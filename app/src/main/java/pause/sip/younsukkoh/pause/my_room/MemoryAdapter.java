package pause.sip.younsukkoh.pause.my_room;

import android.content.Context;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.security.PrivateKey;

import pause.sip.younsukkoh.pause.pojo.Memory;

/**
 * Created by Younsuk on 8/5/2016.
 */
public class MemoryAdapter extends FirebaseRecyclerAdapter<Memory, MemoryViewHolder> {

    private static final String TAG = MemoryAdapter.class.getSimpleName();
    private Context mContext;
    private String mUserEncodedEmail;
    private String mRoomId;

    public MemoryAdapter(Class<Memory> modelClass, int modelLayout, Class<MemoryViewHolder> viewHolderClass, DatabaseReference ref, Context context, String userEncodedEmail, String roomId) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        mContext = context;
        mUserEncodedEmail = userEncodedEmail;
        mRoomId = roomId;
    }

    @Override
    protected void populateViewHolder(MemoryViewHolder viewHolder, Memory memory, int position) {
        viewHolder.bindMemory(memory, mContext, mUserEncodedEmail, mRoomId);
    }
}
