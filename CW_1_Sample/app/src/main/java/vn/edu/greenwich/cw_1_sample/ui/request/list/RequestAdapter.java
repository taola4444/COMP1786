package vn.edu.greenwich.cw_1_sample.ui.request.list;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firestore.admin.v1.Index;

import java.util.ArrayList;

import io.grpc.Context;
import vn.edu.greenwich.cw_1_sample.R;
import vn.edu.greenwich.cw_1_sample.database.ResimaDAO;
import vn.edu.greenwich.cw_1_sample.models.Request;
import vn.edu.greenwich.cw_1_sample.ui.request.RequestUpdateFragment;
import vn.edu.greenwich.cw_1_sample.ui.resident.ResidentUpdateFragment;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> implements Filterable {
    protected ArrayList<Request> _originalList;
    protected ArrayList<Request> _filteredList;
    protected ResimaDAO _db;
    protected RequestAdapter.ItemFilter _itemFilter = new RequestAdapter.ItemFilter();

    public RequestAdapter(ArrayList<Request> list) {
        _originalList = list;
        _filteredList = list;
    }

    public void updateList(ArrayList<Request> list) {
        _originalList = list;
        _filteredList = list;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_request, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        _db = new ResimaDAO(recyclerView.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Request request = _filteredList.get(position);

        holder.listItemRequestDate.setText(request.getDate());
        holder.listItemRequestTime.setText(request.getTime());
        holder.listItemRequestType.setText(request.getType());
        holder.listItemRequestContent.setText(request.getContent());
        holder.listItemRequestTotal.setText(String.valueOf((request.getAmount() * request.getPrice())));
        holder.btnListItemDelete.setOnClickListener(v -> {
            long numOfDeletedRows = _db.deleteRequest(Long.valueOf(request.getId()));
            if (numOfDeletedRows > 0) {
                Toast.makeText(holder.itemView.getContext(), "Delete success!!!", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(v).navigateUp();
                return;
            }
        });
        holder.btnListItemEdit.setOnClickListener(v -> {
            Bundle bundle = null;

            if (request != null) {
                bundle = new Bundle();
                bundle.putSerializable(RequestUpdateFragment.ARG_PARAM_REQUEST, request);
            }

            Navigation.findNavController(v).navigate(R.id.requestUpdateFragment, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return _filteredList == null ? 0 : _filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return _itemFilter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView listItemRequestDate, listItemRequestTime, listItemRequestType, listItemRequestContent,listItemRequestTotal;
        protected ImageView btnListItemDelete,btnListItemEdit;

        public ViewHolder(View itemView) {
            super(itemView);

            listItemRequestDate = itemView.findViewById(R.id.listItemRequestDate);
            listItemRequestTime = itemView.findViewById(R.id.listItemRequestTime);
            listItemRequestType = itemView.findViewById(R.id.listItemRequestType);
            listItemRequestContent = itemView.findViewById(R.id.listItemRequestContent);
            listItemRequestTotal = itemView.findViewById(R.id.listItemRequestTotal);
            btnListItemDelete = itemView.findViewById(R.id.btnListItemDelete);
            btnListItemEdit = itemView.findViewById(R.id.btnListItemEdit);
        }
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            final ArrayList<Request> list = _originalList;
            final ArrayList<Request> nlist = new ArrayList<>(list.size());

            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();

            for (Request request : list) {
                String filterableString = request.toString();

                if (filterableString.toLowerCase().contains(filterString))
                    nlist.add(request);
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            _filteredList = (ArrayList<Request>) results.values;
            notifyDataSetChanged();
        }
    }
}