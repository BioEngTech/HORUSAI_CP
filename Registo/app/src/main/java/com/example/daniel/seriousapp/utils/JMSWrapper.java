package com.example.daniel.seriousapp.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daniel.seriousapp.JMS.ActivityTest;
import com.example.daniel.seriousapp.R;
import com.kaazing.gateway.jms.client.JmsConnectionFactory;
import com.kaazing.net.auth.BasicChallengeHandler;
import com.kaazing.net.auth.ChallengeHandler;
import com.kaazing.net.auth.LoginHandler;
import com.kaazing.net.ws.WebSocketFactory;

import java.net.PasswordAuthentication;
import java.net.URI;
import java.util.ArrayDeque;
import java.util.Enumeration;
import java.util.HashMap;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * Created by joaosousa on 26/05/18.
 */

public class JMSWrapper {

    private JmsConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;
    private Handler handler;

    private HashMap<String, ArrayDeque<MessageConsumer>> consumers = new HashMap<>();
    private StringBuffer sb = new StringBuffer();

    private Activity logActivity;
    private TextView logTextView;

    public JMSWrapper(Activity logActivity) {
        this.logActivity = logActivity;
    }

    public void setLogTextView(TextView logTextView) {
        this.logTextView = logTextView;
    }

    public JmsConnectionFactory createConnectionFactory() throws JMSException {
        if (connectionFactory != null) {
            return connectionFactory;
        }
        connectionFactory = JmsConnectionFactory.createConnectionFactory();
        WebSocketFactory webSocketFactory = connectionFactory.getWebSocketFactory();
        webSocketFactory.setDefaultChallengeHandler(createChallengehandler());

        return connectionFactory;
    }

    private ChallengeHandler createChallengehandler() {
        LoginHandler loginHandler = new LoginHandler() {
            @Override
            public PasswordAuthentication getCredentials() {
                return new PasswordAuthentication(null, null);
            }
        };

        return BasicChallengeHandler
                .create()
                .setLoginHandler(loginHandler);
    }

    private static Destination getDestination(Context context, Session session) throws JMSException {
        String destinationName = context.getResources().getString(R.string.TOPIC_NAME);
        Destination destination;
        if (destinationName.startsWith(context.getResources().getString(R.string.TOPIC_PREFIX))) {
            destination = session.createTopic(destinationName);
        } else if (destinationName.startsWith(context.getResources().getString(R.string.QUEUE_PREFIX))) {
            destination = session.createQueue(destinationName);
        } else {
            Toast.makeText(context, "Invalid destination name: \" + destinationName+\". Destination should start from '/topic/' or '/queue/'", Toast.LENGTH_SHORT).show();
            return null;
        }
        return destination;
    }

    public void doJMSMagic(final Context context) {
        HandlerThread handlerThread = new HandlerThread("handlerThread");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        handler.post(new Runnable() {
            public void run() {
                try {
                    connectionFactory.setGatewayLocation(URI.create(context.getResources().getString(R.string.URL_ADDRESS)));
                    connection = connectionFactory.createConnection();
                    connection.start();
                    session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                    Toast.makeText(context, context.getResources().getString(R.string.connected_state), Toast.LENGTH_SHORT).show();
                    //TODO: Shady stuff here!
//                    ((Activity) context).getWindow().findViewById(R.id.send_btn).setEnabled(true);

                    connection.setExceptionListener(new ExceptionListener() {
                        @Override
                        public void onException(JMSException exception) {
                            Toast.makeText(context, exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    Destination destination = getDestination(context, session);
                    if (destination == null) {
                        return;
                    }

                    MessageConsumer consumer = session.createConsumer(destination);
                    ArrayDeque<MessageConsumer> consumersToDestination = consumers.get(context.getResources().getString(R.string.TOPIC_NAME));

                    if (consumersToDestination == null) {
                        consumersToDestination = new ArrayDeque<>();
                        consumers.put(context.getResources().getString(R.string.TOPIC_NAME), consumersToDestination);
                    }
                    consumersToDestination.add(consumer);
                    consumer.setMessageListener(new DestinationMessageListener());

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "EXCEPTION: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void sendMessage(final Context context) {
        handler.post(new Runnable() {
            public void run() {
                try {
                    MessageProducer producer = session.createProducer(getDestination(context, session));
                    Message message;

                    message = session.createTextMessage("Madrid vs Liverpool");

                    producer.send(message);
                    producer.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public String disconnect(final Context context) {
        if (connection == null) {
            return "";
        }

        sb.append(context.getResources().getString(R.string.disconnecting)).append("\n");

        new Thread(new Runnable() {
            public void run() {
                try {
                    connection.close();
                    sb.append(context.getResources().getString(R.string.disconnected_state)).append("\n");
                    //TODO: Shady stuff here!
//                    ((Activity) context).getWindow().findViewById(R.id.send_btn).setEnabled(false);
                } catch (JMSException e) {
                    e.printStackTrace();
                    sb.append("EXCEPTION: " + e.getMessage());
                } finally {
                    connection = null;
                }
            }
        }).start();

        return sb.toString();
    }

    private class DestinationMessageListener implements MessageListener {

        public void onMessage(Message message) {
            sb.setLength(0);
            try {
                if (message instanceof TextMessage) {
                    sb.append("RECEIVED TextMessage: ").append(((TextMessage) message).getText()).append("\n");
                } else if (message instanceof BytesMessage) {
                    BytesMessage bytesMessage = (BytesMessage) message;

                    long len = bytesMessage.getBodyLength();
                    byte b[] = new byte[(int) len];
                    bytesMessage.readBytes(b);

                    sb.append("RECEIVED BytesMessage: ").append(hexDump(b)).append("\n");
                } else if (message instanceof MapMessage) {
                    MapMessage mapMessage = (MapMessage) message;
                    Enumeration mapNames = mapMessage.getMapNames();
                    while (mapNames.hasMoreElements()) {
                        String key = (String) mapNames.nextElement();
                        Object value = mapMessage.getObject(key);

                        if (value == null) {
                            sb.append(key).append(": null").append("\n");
                        } else if (value instanceof byte[]) {
                            byte[] arr = (byte[]) value;
                            StringBuilder s = new StringBuilder();
                            s.append("[");
                            for (int i = 0; i < arr.length; i++) {
                                if (i > 0) {
                                    s.append(",");
                                }
                                s.append(arr[i]);
                            }
                            s.append("]");
                            sb.append(key).append(": ").append(s.toString()).append(" (Byte[])").append("\n");
                        } else {
                            sb.append(key).append(": ").append(value.toString()).append(" (").append(value.getClass().getSimpleName()).append(")").append("\n");
                        }
                    }
                    sb.append("RECEIVED MapMessage: ").append("\n");
                } else {
                    sb.append("UNKNOWN MESSAGE TYPE: ").append(message.getClass().getSimpleName()).append("\n");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                sb.append("EXCEPTION: ").append(ex.getMessage()).append("\n");
            }

            logActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //TODO: Very shady stuff here!
                    ((ActivityTest) logActivity).logMessage(sb.toString());
                }
            });
        }

        private String hexDump(byte[] bytes) {
            if (bytes.length == 0) {
                return "empty";
            }

            StringBuilder out = new StringBuilder();
            for (byte bite : bytes) {
                out.append(Integer.toHexString(bite)).append(' ');
            }
            return out.toString();
        }
    }


}
