package com.frederlen.android.pittnotes;

/**
 * Created by Fred on 3/13/18.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.frederlen.android.pittnotes.models.Session;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.SessionAdapterViewHolder> {

    private List<Session> mSessionData;

    private final SessionAdapterOnClickHandler mClickHandler;

    public interface SessionAdapterOnClickHandler {
        void onClick(Session session);
    }

    public SessionAdapter(SessionAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class SessionAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mSessionTextView;

        public SessionAdapterViewHolder(View view) {
            super(view);
            mSessionTextView = (TextView) view.findViewById(R.id.tv_session_data);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Session session = mSessionData.get(adapterPosition);
            mClickHandler.onClick(session);
        }
    }


    @Override
    public SessionAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.session_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new SessionAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SessionAdapterViewHolder sessionAdapterViewHolder, int position) {
        String sessionInfo = mSessionData.get(position).getSessionDate();
        sessionAdapterViewHolder.mSessionTextView.setText(sessionInfo);
    }

    @Override
    public int getItemCount() {
        if (null == mSessionData) return 0;
        return mSessionData.size();
    }

    public void setSessionData(List<Session> sessionData) {
        mSessionData = sessionData;
        notifyDataSetChanged();
    }
}
