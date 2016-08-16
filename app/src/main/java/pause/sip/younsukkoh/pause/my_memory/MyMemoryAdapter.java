package pause.sip.younsukkoh.pause.my_memory;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import pause.sip.younsukkoh.pause.pojo.Memory;

/**
 * Created by Younsuk on 8/5/2016.
 */
public class MyMemoryAdapter extends FirebaseRecyclerAdapter<Memory, MyMemoryViewHolder> {

    private static final String TAG = MyMemoryAdapter.class.getSimpleName();
    private String mUserEncodedEmail;

    public MyMemoryAdapter(Class<Memory> modelClass, int modelLayout, Class<MyMemoryViewHolder> viewHolderClass, DatabaseReference ref, String userEncodedEmail) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        mUserEncodedEmail = userEncodedEmail;
    }

    public MyMemoryAdapter(Class<Memory> modelClass, int modelLayout, Class<MyMemoryViewHolder> viewHolderClass, Query ref, String userEncodedEmail) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        mUserEncodedEmail = userEncodedEmail;
    }

    @Override
    protected void populateViewHolder(MyMemoryViewHolder viewHolder, Memory memory, int position) {
        viewHolder.bindMemory(memory, mUserEncodedEmail);
    }
}