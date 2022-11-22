package vn.edu.greenwich.cw_1_sample.ui.request;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.annotation.Native;

import vn.edu.greenwich.cw_1_sample.R;
import vn.edu.greenwich.cw_1_sample.database.ResimaDAO;
import vn.edu.greenwich.cw_1_sample.models.Request;
import vn.edu.greenwich.cw_1_sample.models.Resident;
import vn.edu.greenwich.cw_1_sample.ui.dialog.CalendarFragment;
import vn.edu.greenwich.cw_1_sample.ui.dialog.TimePickerFragment;
import vn.edu.greenwich.cw_1_sample.ui.resident.ResidentRegisterFragment;

public class RequestUpdateFragment extends Fragment {
    public static final String ARG_PARAM_REQUEST = "request";
    protected ResimaDAO _db;
    protected EditText fmRequestEditDate,fmRequestEditTime,fmRequestEditPrice,
            fmRequestEditAmount,fmRequestEditContent;
    protected Button fmRequestEditButton;
    protected Request request;
    protected Spinner fmRequestEditName;

    public RequestUpdateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_update, container, false);

        fmRequestEditName = view.findViewById(R.id.fmRequestEditName);
        fmRequestEditDate = view.findViewById(R.id.fmRequestEditDate);
        fmRequestEditTime = view.findViewById(R.id.fmRequestEditTime);
        fmRequestEditPrice = view.findViewById(R.id.fmRequestEditPrice);
        fmRequestEditAmount = view.findViewById(R.id.fmRequestEditAmount);
        fmRequestEditContent = view.findViewById(R.id.fmRequestEditContent);

        fmRequestEditButton = view.findViewById(R.id.fmRequestEditButton);
        if (getArguments() != null) {
            request = (Request) getArguments().getSerializable(ARG_PARAM_REQUEST);


            fmRequestEditName.setAdapter(
                    ArrayAdapter.createFromResource(
                    getContext(),
                    R.array.request_type,
                    android.R.layout.simple_spinner_item
            ));
            fmRequestEditDate.setText(request.getDate());
            fmRequestEditTime.setText(request.getTime());
            fmRequestEditPrice.setText(String.valueOf(request.getPrice()));
            fmRequestEditAmount.setText(String.valueOf(request.getAmount()));
            fmRequestEditContent.setText(request.getContent());

        }
        fmRequestEditDate.setOnTouchListener((v, motionEvent) -> showCalendar(motionEvent));
        fmRequestEditTime.setOnTouchListener((v, motionEvent) -> showTimeDialog(motionEvent));

        fmRequestEditButton.setOnClickListener(v -> {
            String type = fmRequestEditName.getSelectedItem().toString();
            String date = fmRequestEditDate.getText().toString();
            String time = fmRequestEditTime.getText().toString();
            String price = fmRequestEditPrice.getText().toString();
            String amount = fmRequestEditAmount.getText().toString();
            String content = fmRequestEditContent.getText().toString();
            Request _request = new Request();
            _request.setId(request.getId());
            _request.setResidentId(request.getResidentId());
            _request.setAmount(Integer.parseInt(amount));
            _request.setPrice(Integer.parseInt(price));
            _request.setContent(content);
            _request.setDate(date);
            _request.setTime(time);
            _request.setType(type);
            long status = _db.updateRequest(_request);
            if(status > 0) {
                Toast.makeText(getContext(), "Edit expense success!!!", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(view).navigateUp();
            }
        });
        return view;
    }

    protected boolean showTimeDialog(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            new TimePickerFragment().show(getChildFragmentManager(), null);
            return true;
        }

        return false;
    }

    protected boolean showCalendar(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            new CalendarFragment().show(getChildFragmentManager(), null);
        }

        return false;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        _db = new ResimaDAO(getContext());
    }
}