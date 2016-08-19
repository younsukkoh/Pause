package pause.sip.younsukkoh.pause.my_room;

import android.content.Context;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import pause.sip.younsukkoh.pause.pojo.Memory;

/**
 * Created by Younsuk on 8/5/2016.
 */
public class MemoryAdapter extends FirebaseRecyclerAdapter<Memory, MemoryViewHolder> {

    private static final String TAG = MemoryAdapter.class.getSimpleName();
    private String mUserEncodedEmail;
    private Context mContext;

    public MemoryAdapter(Class<Memory> modelClass, int modelLayout, Class<MemoryViewHolder> viewHolderClass, DatabaseReference ref, String userEncodedEmail, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        mUserEncodedEmail = userEncodedEmail;
        mContext = context;
    }

    public MemoryAdapter(Class<Memory> modelClass, int modelLayout, Class<MemoryViewHolder> viewHolderClass, Query ref, String userEncodedEmail) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        mUserEncodedEmail = userEncodedEmail;
    }

    @Override
    protected void populateViewHolder(MemoryViewHolder viewHolder, Memory memory, int position) {
        viewHolder.bindMemory(memory, mUserEncodedEmail, mContext);
    }
}
