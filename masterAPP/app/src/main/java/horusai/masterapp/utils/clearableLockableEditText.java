package horusai.masterapp.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public class clearableLockableEditText extends AppCompatEditText implements View.OnTouchListener{

	public String defaultValue = "";

	final Drawable imgX = getResources().getDrawable(android.R.drawable.presence_offline); // X image

	public clearableLockableEditText(Context context) {
		super(context);
		init();
	}

	public clearableLockableEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public clearableLockableEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	void init() {

		// Set bounds of our X button

		imgX.setBounds(0, 0, imgX.getIntrinsicWidth(), imgX.getIntrinsicHeight());

		// There may be initial text in the field, so we may need to display the button

		manageClearButton();

		setOnTouchListener(this);

		this.addTextChangedListener(generalTextWatcher);

	}

	// Implement generalOnTouchListener


	@Override
	public boolean onTouch(View v, MotionEvent event) {

			clearableLockableEditText et = clearableLockableEditText.this;

			// Is there an X showing?
			if (et.getCompoundDrawables()[2] == null) return false;
			// Only do this for up touches
			if (event.getAction() != MotionEvent.ACTION_UP) return false;
			// Is touch on our clear button?
			if (event.getX() > et.getWidth() - et.getPaddingRight() - imgX.getIntrinsicWidth()) {
				et.setText("");
				clearableLockableEditText.this.removeClearButton();
			}
			return false;
	}

	// Implement TextWatcher

	private TextWatcher generalTextWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

			clearableLockableEditText.this.manageClearButton();
		}

		@Override
		public void afterTextChanged(Editable arg0) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}
	};

	void manageClearButton() {
		if (this.getText().toString().equals("") )
			removeClearButton();
		else
			addClearButton();
	}
	void addClearButton() {
		this.setCompoundDrawables(this.getCompoundDrawables()[0],
				this.getCompoundDrawables()[1],
				imgX,
				this.getCompoundDrawables()[3]);

	}
	void removeClearButton() {
		this.setCompoundDrawables(this.getCompoundDrawables()[0],
				this.getCompoundDrawables()[1],
				null,
				this.getCompoundDrawables()[3]);
	}

	@Override
	public boolean onKeyPreIme(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return false;
	}

}