package pause.sip.younsukkoh.pause.editor;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import pause.sip.younsukkoh.pause.R;
import pause.sip.younsukkoh.pause.utility.Constants;

/**
 * Created by Younsuk on 8/21/2016.
 */
public class EditDateTimeDialogFragment extends DialogFragment {

    private long mDateTime;
    private DatePicker mDatePicker;
    private TimePicker mTimePicker;

    /**
     * Initialize Edit Date Time Dialog Fragment
     */
    public static EditDateTimeDialogFragment newInstance(long dateTime) {
        Bundle args = new Bundle();
        args.putLong(Constants.ARG_DATE_TIME, dateTime);

        EditDateTimeDialogFragment fragment = new EditDateTimeDialogFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDateTime = getArguments().getLong(Constants.ARG_DATE_TIME);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.edit_date_time_dialog_fragment, null);

        setUpUI(view);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.edit_date_time)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int year = mDatePicker.getYear();
                        int month = mDatePicker.getMonth();
                        int day = mDatePicker.getDayOfMonth();
                        int hour = mTimePicker.getCurrentHour();
                        int minute = mTimePicker.getCurrentMinute();

                        sendResultOk(new GregorianCalendar(year, month, day, hour, minute).getTime());
                    }
                });
        alertDialogBuilder.setView(view);

        return alertDialogBuilder.create();
    }

    /**
     * Set up user interface
     * @param view
     */
    private void setUpUI(View view) {
        Date date = new Date(mDateTime);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);

        mDatePicker = (DatePicker) view.findViewById(R.id.edtdf_dp);
        mDatePicker.init(year, month, day, null);

        mTimePicker = (TimePicker) view.findViewById(R.id.edtdf_tp);
        mTimePicker.setIs24HourView(false);
        mTimePicker.setCurrentHour(hour);
        mTimePicker.setCurrentMinute(minute);
    }

    /**
     * Send result of date picker and time picker to EditMemoryDialogFragment when user clicks OK
     * @param dateTime
     */
    private void sendResultOk(Date dateTime) {
        if (getTargetFragment() == null) return;

        Intent intent = new Intent();
        intent.putExtra(Constants.EXTRA_DATE_TIME, dateTime);

        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
    }
}