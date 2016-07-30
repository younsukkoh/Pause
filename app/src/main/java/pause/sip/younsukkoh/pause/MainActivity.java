package pause.sip.younsukkoh.pause;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends BaseActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Intent intent = new Intent(MainActivity.this, CreateAccountActivity01.class);
//        startActivity(intent);

//        mTextView = (TextView) findViewById(R.id.textView);
//        mTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.i(Constants.TAG, "YOLO");
//                TesterPojo pojo = new TesterPojo("Sucker Punch");
//                mDatabase.child("new").push().setValue(pojo);
//            }
//        });
    }
}
