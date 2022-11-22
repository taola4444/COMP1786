package vn.edu.greenwich.cw_1_sample.ui.resident;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;

import vn.edu.greenwich.cw_1_sample.R;
import vn.edu.greenwich.cw_1_sample.database.ResimaDAO;
import vn.edu.greenwich.cw_1_sample.models.Resident;
import vn.edu.greenwich.cw_1_sample.ui.dialog.CalendarFragment;

public class ResidentRegisterFragment extends Fragment
        implements ResidentRegisterConfirmFragment.FragmentListener, CalendarFragment.FragmentListener {
    public static final String ARG_PARAM_RESIDENT = "resident";

    protected EditText fmResidentRegisterName, fmResidentRegisterAdvice, fmResidentRegisterStartDate,fmResidentRegisterDestination,fmResidentRegisterDescription;
    protected LinearLayout fmResidentRegisterLinearLayout;
    protected SwitchMaterial fmResidentRegisterOwner;
    protected TextView fmResidentRegisterError;
    protected Button fmResidentRegisterButton;
    protected Spinner fmResidentRegisterQuality,fmResidentRegisterVehicle;

    protected ResimaDAO _db;

    public ResidentRegisterFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        _db = new ResimaDAO(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resident_register, container, false);

        fmResidentRegisterError = view.findViewById(R.id.fmResidentRegisterError);
        fmResidentRegisterName = view.findViewById(R.id.fmResidentRegisterName);
        fmResidentRegisterStartDate = view.findViewById(R.id.fmResidentRegisterStartDate);
        fmResidentRegisterOwner = view.findViewById(R.id.fmResidentRegisterOwner);
        fmResidentRegisterDestination = view.findViewById(R.id.fmResidentRegisterDestination);
        fmResidentRegisterDescription = view.findViewById(R.id.fmResidentRegisterDescription);
        fmResidentRegisterButton = view.findViewById(R.id.fmResidentRegisterButton);
        fmResidentRegisterLinearLayout = view.findViewById(R.id.fmResidentRegisterLinearLayout);
        fmResidentRegisterQuality = view.findViewById(R.id.fmResidentRegisterQuality);
        fmResidentRegisterVehicle = view.findViewById(R.id.fmResidentRegisterVehicle);
        fmResidentRegisterAdvice = view.findViewById(R.id.fmResidentRegisterAdvice);

        // Show Calendar for choosing a date.
        fmResidentRegisterStartDate.setOnTouchListener((v, motionEvent) -> showCalendar(motionEvent));

        // Update current resident.
        if (getArguments() != null) {
            Resident resident = (Resident) getArguments().getSerializable(ARG_PARAM_RESIDENT);

            fmResidentRegisterName.setText(resident.getName());
            fmResidentRegisterStartDate.setText(resident.getStartDate());
            fmResidentRegisterOwner.setChecked(resident.getOwner() == 1 ? true : false);
            fmResidentRegisterDestination.setText(resident.getDestination());
            fmResidentRegisterDescription.setText(resident.getDescription());
            fmResidentRegisterQuality.setAdapter(
                    ArrayAdapter.createFromResource(
                            getContext(),
                            R.array.request_type,
                            android.R.layout.simple_spinner_item
                    )
            );
            fmResidentRegisterVehicle.setAdapter(
                    ArrayAdapter.createFromResource(
                            getContext(),
                            R.array.resident_vehicle,
                            android.R.layout.simple_spinner_item
                    )
            );
            fmResidentRegisterAdvice.setText(resident.getAdvice());
            fmResidentRegisterButton.setText(R.string.label_update);
            fmResidentRegisterButton.setOnClickListener(v -> update(resident.getId()));

            return view;
        }
        setQualitySpinner();
        setVehicleSpinner();
        // Create new resident.
        fmResidentRegisterButton.setOnClickListener(v -> register());

        return view;
    }

    private void setVehicleSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.resident_vehicle,
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fmResidentRegisterVehicle.setAdapter(adapter);
    }

    private void setQualitySpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.resident_quality,
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fmResidentRegisterQuality.setAdapter(adapter);
    }

    protected void register() {
        if (isValidForm()) {
            Resident resident = getResidentFromInput(-1);

            new ResidentRegisterConfirmFragment(resident).show(getChildFragmentManager(), null);

            return;
        }

        moveButton();
    }

    protected void update(long id) {
        if (isValidForm()) {
            Resident resident = getResidentFromInput(id);
            Log.d("resident", String.valueOf(resident.getQuality()));
            long status = _db.updateResident(resident);

            FragmentListener listener = (FragmentListener) getParentFragment();
            listener.sendFromResidentRegisterFragment(status);

            return;
        }

        moveButton();
    }

    protected boolean showCalendar(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            new CalendarFragment().show(getChildFragmentManager(), null);
        }

        return false;
    }

    protected Resident getResidentFromInput(long id) {
        String name = fmResidentRegisterName.getText().toString();
        String startDate = fmResidentRegisterStartDate.getText().toString();
        int owner = fmResidentRegisterOwner.isChecked() ? 1 : 0;
        String destination = fmResidentRegisterDestination.getText().toString();
        String description = fmResidentRegisterDescription.getText().toString();
        String quality = fmResidentRegisterQuality.getSelectedItem().toString();
        String vehicle = fmResidentRegisterVehicle.getSelectedItem().toString();
        String advice = fmResidentRegisterAdvice.getText().toString();

        return new Resident(id, name,destination,description, startDate, owner,vehicle,quality,advice);
    }

    protected boolean isValidForm() {
        boolean isValid = true;

        String error = "";
        String name = fmResidentRegisterName.getText().toString();
        String startDate = fmResidentRegisterStartDate.getText().toString();
        String destination = fmResidentRegisterDestination.getText().toString();
        String description = fmResidentRegisterDescription.getText().toString();
        String advice = fmResidentRegisterAdvice.getText().toString();


        if (name == null || name.trim().isEmpty()) {
            error += "* " + getString(R.string.error_blank_name) + "\n";
            isValid = false;
        }

        if (startDate == null || startDate.trim().isEmpty()) {
            error += "* " + getString(R.string.error_blank_start_date) + "\n";
            isValid = false;
        }

        if (destination == null || destination.trim().isEmpty()) {
            error += "* " + getString(R.string.error_blank_destination) + "\n";
            isValid = false;
        }
        if (description == null || description.trim().isEmpty()) {
            error += "* " + getString(R.string.error_blank_description) + "\n";
            isValid = false;
        }

        if (advice == null || advice.trim().isEmpty()) {
            error += "* " + getString(R.string.error_blank_advice) + "\n";
            isValid = false;
        }


        fmResidentRegisterError.setText(error);

        return isValid;
    }

    protected void moveButton() {
        LinearLayout.LayoutParams btnParams = (LinearLayout.LayoutParams) fmResidentRegisterButton.getLayoutParams();

        int linearLayoutPaddingLeft = fmResidentRegisterLinearLayout.getPaddingLeft();
        int linearLayoutPaddingRight = fmResidentRegisterLinearLayout.getPaddingRight();
        int linearLayoutWidth = fmResidentRegisterLinearLayout.getWidth() - linearLayoutPaddingLeft - linearLayoutPaddingRight;

        btnParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        btnParams.topMargin += fmResidentRegisterButton.getHeight();
        btnParams.leftMargin = btnParams.leftMargin == 0 ? linearLayoutWidth - fmResidentRegisterButton.getWidth() : 0;

        fmResidentRegisterButton.setLayoutParams(btnParams);
    }

    @Override
    public void sendFromResidentRegisterConfirmFragment(long status) {
        switch ((int) status) {
            case -1:
                Toast.makeText(getContext(), R.string.notification_create_fail, Toast.LENGTH_SHORT).show();
                return;

            default:
                Toast.makeText(getContext(), R.string.notification_create_success, Toast.LENGTH_SHORT).show();

                fmResidentRegisterName.setText("");
                fmResidentRegisterStartDate.setText("");
                fmResidentRegisterDescription.setText("");
                fmResidentRegisterDestination.setText("");
                fmResidentRegisterAdvice.setText("");

                fmResidentRegisterName.requestFocus();
        }
    }

    @Override
    public void sendFromCalendarFragment(String date) {
        fmResidentRegisterStartDate.setText(date);
    }

    public interface FragmentListener {
        void sendFromResidentRegisterFragment(long status);
    }
}