package com.example.daniel.seriousapp.JMS;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daniel.seriousapp.R;
import com.example.daniel.seriousapp.utils.UserType;
import com.kaazing.gateway.jms.client.JmsConnectionFactory;
import com.kaazing.net.auth.BasicChallengeHandler;
import com.kaazing.net.auth.ChallengeHandler;
import com.kaazing.net.auth.LoginHandler;
import com.kaazing.net.ws.WebSocketFactory;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.util.ArrayDeque;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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
import javax.jms.ObjectMessage;
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

    private UserType userType = UserType.CARE_TAKER;

    public JMSWrapper(Activity logActivity) {
        this.logActivity = logActivity;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public void setLogTextView(TextView logTextView) {
        this.logTextView = logTextView;
    }

    public void createConnectionFactory() throws JMSException {
        if (connectionFactory != null) {
            return;
        }
        connectionFactory = JmsConnectionFactory.createConnectionFactory();
        WebSocketFactory webSocketFactory = connectionFactory.getWebSocketFactory();
        webSocketFactory.setDefaultChallengeHandler(createChallengehandler());
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

    private static Destination getCareProviderDestination(Context context, Session session) throws JMSException {
        return getDestination(context, context.getResources().getString(R.string.TOPIC_CARE_PROVIDER), session);
    }

    private static Destination getCareTakerDestination(Context context, Session session) throws JMSException {
        return getDestination(context, context.getResources().getString(R.string.TOPIC_CARE_TAKER), session);
    }

    private static Destination getDestination(Context context, String destinationName, Session session) throws JMSException {
        if (destinationName.startsWith(context.getResources().getString(R.string.TOPIC_PREFIX))) {
            return session.createTopic(destinationName);
        } else if (destinationName.startsWith(context.getResources().getString(R.string.QUEUE_PREFIX))) {
            return session.createQueue(destinationName);
        } else {
            Toast.makeText(context, "Invalid destination name: \" + destinationName+\". Destination should start from '/topic/' or '/queue/'", Toast.LENGTH_SHORT).show();
            return null;
        }
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

                    connection.setExceptionListener(new ExceptionListener() {
                        @Override
                        public void onException(JMSException exception) {
                            Toast.makeText(context, exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    Destination destination;
                    ArrayDeque<MessageConsumer> consumersToDestination = new ArrayDeque<>();
                    String topicName;
                    switch (userType) {
                        case CARE_TAKER:
                            destination = getCareTakerDestination(context, session);
                            topicName = context.getResources().getString(R.string.TOPIC_CARE_PROVIDER);
                            break;
                        case CARE_PROVIDER:
                            destination = getCareProviderDestination(context, session);
                            topicName = context.getResources().getString(R.string.TOPIC_CARE_TAKER);
                            break;
                        default:
                            return;
                    }

                    consumersToDestination = consumers.get(topicName);
                    if (consumersToDestination == null) {
                        consumersToDestination = new ArrayDeque<>();
                        consumers.put(topicName, consumersToDestination);
                    }
                    MessageConsumer consumer = session.createConsumer(destination);
                    consumersToDestination.add(consumer);
                    consumer.setMessageListener(new DestinationMessageListener());

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "EXCEPTION: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void sendMessage(final Context context, final JMSObject messageObject) {
        handler.post(new Runnable() {
            public void run() {
                try {
                    ObjectMessage message = session.createObjectMessage(messageObject);
                    MessageProducer producer = session.createProducer(userType == UserType.CARE_TAKER ?
                            getCareProviderDestination(context, session) : getCareTakerDestination(context, session));

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
                } catch (JMSException e) {
                    e.printStackTrace();
                    sb.append("EXCEPTION: ").append(e.getMessage());
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

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    CharSequence name = "name";
                    String description = "description";
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                    NotificationChannel channel = new NotificationChannel("001", name, importance);
                    channel.setDescription(description);
                    NotificationManager notificationManager = logActivity.getSystemService(NotificationManager.class);
                    notificationManager.createNotificationChannel(channel);
                }

                Intent intent = new Intent(logActivity, JMSWrapper.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(logActivity, 0, intent, 0);

                if (message instanceof TextMessage) {
                    sb.append("RECEIVED TextMessage: ").append(((TextMessage) message).getText()).append("\n");
                } else if (message instanceof BytesMessage) {
                    BytesMessage bytesMessage = (BytesMessage) message;

                    long len = bytesMessage.getBodyLength();
                    byte b[] = new byte[(int) len];
                    bytesMessage.readBytes(b);
                    JMSObject object = (JMSObject) new ObjectInputStream(new ByteArrayInputStream(b)).readObject();
                    Geocoder gcd = new Geocoder(logActivity, Locale.getDefault());
                    List<Address> addresses = gcd.getFromLocation(object.getLocation().getLatitude(), object.getLocation().getLongitude(), 1);

                    if (addresses.size() > 0) {
                        sb.append("City ");
                        sb.append(addresses.get(0).getLocality()).append("\n");
                    }
                    sb.append("Event Scenario: ").append(object.getEventScenario().toString()).append("\n");
                    if (object.getPatientBackground() != null)sb.append("Patient background: ").append(object.getPatientBackground()).append("\n");
                    if (object.getExtraNotes() != null)sb.append("Extra notes: ").append(object.getExtraNotes());

                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(logActivity.getBaseContext(), "001")
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setContentTitle("Alert in" + addresses.get(0).getLocality())
                            .setContentText("Trouble Alert")
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(sb.toString()))
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setContentIntent(pendingIntent);

                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(logActivity);
                    // notificationId is a unique int for each notification that you must define
                    notificationManager.notify(1, mBuilder.build());



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
