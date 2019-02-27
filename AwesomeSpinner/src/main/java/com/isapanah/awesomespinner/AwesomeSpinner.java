package com.isapanah.awesomespinner;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by sadra on 5/26/17.
 */

public class AwesomeSpinner extends RelativeLayout {

    private AppCompatButton _hintButton;
    private spinnerDefaultSelection _spinner;
    private ImageView _downArrow;
    private ArrayAdapter<String> _spinnerAdapterString;
    private ArrayAdapter<CharSequence> _spinnerAdapterCharSequence;
    private boolean _allowToSelect;
    private onSpinnerItemClickListener<String> _callback;
    private static final String TAG = AwesomeSpinner.class.getSimpleName();
    private boolean _isItemResourceDeclared = false;
    private int _spinnerType = 0;
    private boolean _isSelected;
    private int HINT_BUTTON_NOT_SELECTED_COLOR = Color.parseColor("#aaaaaa");
    private final int HINT_BUTTON_DISABLED_COLOR = Color.parseColor("#BDBDBD");
    private int HINT_BUTTON_COLOR = Color.BLACK;
    private final int DOWN_ARROW_DEFAULT_TINT_COLOR = Color.parseColor("#797979");
    private int DOWN_ARROW_TINT_COLOR = Color.parseColor("#797979");

    public AwesomeSpinner(Context context) {
        super(context);
        init(null);
    }

    public AwesomeSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public AwesomeSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.spinner_view, this);

        this._hintButton = (AppCompatButton) findViewById(R.id.awesomeSpinner_hintButton);
        this._spinner = (spinnerDefaultSelection) findViewById(R.id.awesomeSpinner_spinner);
        this._downArrow = (ImageView) findViewById(R.id.awesomeSpinner_downArrow);

        if (attrs != null) {
            setSpinnerStyle(getContext().obtainStyledAttributes(attrs, R.styleable.AwesomeSpinnerStyle, 0, 0));
        }

    }


    private void setSpinnerStyle(TypedArray typedArray) {

        setHintButtonText(typedArray.getString(R.styleable.AwesomeSpinnerStyle_spinnerHint));
        setHintTextSize(typedArray.getDimensionPixelSize(R.styleable.AwesomeSpinnerStyle_spinnerHintTextSize, 0));

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 40, 10, 10);
        switch (typedArray.getInt(R.styleable.AwesomeSpinnerStyle_spinnerDirection, 0)) {

            case 0:
                _hintButton.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                _downArrow.setLayoutParams(params);
                break;
            case 1:
                _hintButton.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                _downArrow.setLayoutParams(params);

                break;
        }

    }

    public void setAdapter(ArrayAdapter<String> adapter) {
        this._spinnerAdapterString = adapter;
        _spinner.setAdapter(_spinnerAdapterString);
        initiateSpinnerString();
    }

    public void setAdapter(ArrayAdapter<CharSequence> adapter, int idle) {
        _spinnerType = 1;
        this._spinnerAdapterCharSequence = adapter;
        _spinner.setAdapter(_spinnerAdapterCharSequence);
        initiateSpinnerCharSequence();
    }

    public boolean isSelected() {
        return _isSelected;
    }

    private void initiateSpinnerString() {

        if (!_isItemResourceDeclared) {
            _spinnerAdapterString.setDropDownViewResource(R.layout.spinner_list_item);
        }

        _spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Log.d(TAG, "position selected: " + position);
                if (AwesomeSpinner.this._callback == null) {
                    throw new IllegalStateException("callback cannot be null");
                }
                if (_allowToSelect) {
                    _isSelected = true;
                    Object item = AwesomeSpinner.this._spinner.getItemAtPosition(position);
                    AwesomeSpinner.this._callback.onItemSelected(position, (String) item);
                    setHintButtonText(_spinner.getSelectedItem().toString());
                }
                _allowToSelect = true;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "Nothing selected");
            }
        });


    }

    private void initiateSpinnerCharSequence() {

        if (!_isItemResourceDeclared) {
            _spinnerAdapterCharSequence.setDropDownViewResource(R.layout.spinner_list_item);
        }

        _spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Log.d(TAG, "position selected: " + position);
                if (AwesomeSpinner.this._callback == null) {
                    throw new IllegalStateException("callback cannot be null");
                }
                if (_allowToSelect) {
                    _isSelected = true;
                    Object item = AwesomeSpinner.this._spinner.getItemAtPosition(position);
                    AwesomeSpinner.this._callback.onItemSelected(position, (String) item);
                    setHintButtonText(_spinner.getSelectedItem().toString());
                }
                _allowToSelect = true;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "Nothing selected");
            }
        });


    }

    public void setDropDownViewResource(int resource) {

        if (_spinnerType == 1) {
            _spinnerAdapterCharSequence.setDropDownViewResource(resource);
        } else {
            _spinnerAdapterString.setDropDownViewResource(resource);
        }

        _isItemResourceDeclared = true;

    }

    public void setOnSpinnerItemClickListener(onSpinnerItemClickListener<String> callback) {

        this._callback = callback;

        _hintButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                _spinner.performClick();
            }
        });
    }

    public void setSelection(int position) {
        _allowToSelect = true;
        _spinner.setSelection(position);
    }

    public void setSelection(String value) {
        if (value.trim().isEmpty()) {
            _allowToSelect = true;
            if (_spinnerType == 0) {
                int spinnerPosition = _spinnerAdapterString.getPosition(value);
                _spinner.setSelection(spinnerPosition);
            } else {
                int spinnerPosition = _spinnerAdapterCharSequence.getPosition(value);
                _spinner.setSelection(spinnerPosition);
            }
        }
    }

    public String getSelectedItem() {
        if (isSelected()) {
            return _spinner.getSelectedItem().toString();
        } else {
            return null;
        }
    }

    public spinnerDefaultSelection getSpinner() {
        return _spinner;
    }

    public int getSelectedItemPosition() {
        return _spinner.getSelectedItemPosition();
    }

    public interface onSpinnerItemClickListener<T> {
        /**
         * When a spinner item has been selected.
         *
         * @param position       Position selected
         * @param itemAtPosition Item selected
         */
        void onItemSelected(int position, T itemAtPosition);
    }

    public void setSpinnerEnable(boolean enable) {
        this._spinner.setEnabled(enable);
        this._hintButton.setEnabled(enable);
        setDownArrowStyle();
        setHitButtonStyle();
    }

    public boolean isSpinnerEnable() {
        return this._spinner.isEnabled();
    }

    private void setHitButtonStyle() {
        this._hintButton.setTextColor(
                this._hintButton.isEnabled() ?
                        (isSelected() ? HINT_BUTTON_COLOR : HINT_BUTTON_NOT_SELECTED_COLOR)
                        :
                        HINT_BUTTON_DISABLED_COLOR
        );
    }

    public void setHintTextColor(int color) {
        this.HINT_BUTTON_NOT_SELECTED_COLOR = color;
        this._hintButton.setTextColor(isSelected() ? this.HINT_BUTTON_COLOR : this.HINT_BUTTON_NOT_SELECTED_COLOR);
    }

    public void setSelectedItemHintColor(int color) {
        this.HINT_BUTTON_COLOR = color;
        this._hintButton.setTextColor(isSelected() ? this.HINT_BUTTON_COLOR : this.HINT_BUTTON_NOT_SELECTED_COLOR);
    }

    private void setHintButtonText(String label) {
        _hintButton.setText(label);
        setHitButtonStyle();
    }

    private void setHintTextSize(float size) {
        _hintButton.setTextSize(size);
    }

    private void setDownArrowStyle() {
        this._downArrow.setColorFilter(
                this._hintButton.isEnabled() ? this.DOWN_ARROW_TINT_COLOR : this.DOWN_ARROW_DEFAULT_TINT_COLOR,
                PorterDuff.Mode.SRC_ATOP);
        this._downArrow.setAlpha(this._hintButton.isEnabled() ? 1.0f : 0.6f);
    }

    public void setDownArrowTintColor(int color) {
        this.DOWN_ARROW_TINT_COLOR = color;
        setDownArrowStyle();
    }

}
