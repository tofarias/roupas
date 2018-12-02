package com.example.tiago.roupas.fragment.home;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tiago.roupas.R;
import com.example.tiago.roupas.model.Necessidade;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class NecessidadeTabFragment extends Fragment {

    List<Necessidade> necessidadeList = new ArrayList<>();
    NecessidadeAdapter necessidadeAdapter;
    RecyclerView recyclerView;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    private ProgressBar mProgressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_necessidade_tab, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //getActivity().setTitle("Aguardando Doações");

        mProgressBar = (ProgressBar) getActivity().findViewById(R.id.progressBarNecessidade);

        this.setFirebaseInstance();
        this.setDatabaseReference();

        this.recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerViewNecessidades);

        if (this.recyclerView != null)
            this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    /*ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();

            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                Necessidade necessidade = snapshot.getValue(Necessidade.class);

                necessidadeList.add(necessidade);

                Toast.makeText(getContext(), "Titulo: "+necessidade.getTitulo(), Toast.LENGTH_SHORT).show();
            }

            necessidadeAdapter = new NecessidadeAdapter(necessidadeList);

            recyclerView.setAdapter(necessidadeAdapter);
            recyclerView.addItemDecoration(
                    new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };*/

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();

            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                 Necessidade necessidade = snapshot.getValue(Necessidade.class);

                necessidadeList.add(necessidade);

                //Toast.makeText(getContext(), "Titulo: "+necessidade.getTitulo(), Toast.LENGTH_SHORT).show();

            }

            necessidadeAdapter = new NecessidadeAdapter(necessidadeList);

            if (recyclerView != null) {
                recyclerView.setAdapter(necessidadeAdapter);
                recyclerView.addItemDecoration(
                        new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            }

            mProgressBar.setVisibility(View.GONE);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }


    };

    private void setDatabaseReference() {

        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        String uuid = FirebaseAuth.getInstance().getUid();

        this.mDatabaseReference = this.mFirebaseDatabase.getReference("necessidades/" + uuid);
        //this.mDatabaseReference.addChildEventListener(childEventListener);
        this.mDatabaseReference.orderByChild("createdAt")
                               //.startAt("2018-11-30").endAt("2018-12-01")
                               //.limitToLast(4)
                               .addListenerForSingleValueEvent(valueEventListener);
    }

    private void setFirebaseInstance() {

        this.mFirebaseDatabase = FirebaseDatabase.getInstance();
    }

    public class NecessidadeHolder extends RecyclerView.ViewHolder{

        public TextView textViewTitulo, textViewJustificativa, textViewDescricao, textViewCreatedAt, textViewAguardando;

        public NecessidadeHolder(@NonNull final View itemView) {
            super(itemView);

            this.textViewTitulo        = itemView.findViewById(R.id.textViewTitulo);
            this.textViewJustificativa = itemView.findViewById(R.id.textViewJustificativa);
            this.textViewCreatedAt     = itemView.findViewById(R.id.textViewCreatedAt);
            this.textViewAguardando     = itemView.findViewById(R.id.textViewAguardando);

            itemView.setOnClickListener( itemViewOnClickListener );
        }

        private View.OnClickListener itemViewOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) { }
        };
    }

    public class NecessidadeAdapter extends RecyclerView.Adapter<NecessidadeHolder> {
        
        public List<Necessidade> necessidadeList;

        public NecessidadeAdapter(List<Necessidade> m){
            this.necessidadeList = m;
        }

        @NonNull
        @Override
        public NecessidadeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.home_item_necessidade,parent,false );

            return new NecessidadeHolder(view);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onBindViewHolder(@NonNull NecessidadeHolder holder, int position) {

            String titulo        = this.necessidadeList.get(position).getTitulo();
            String justificativa = this.necessidadeList.get(position).getJustificativa();
            String descricao     = this.necessidadeList.get(position).getDescricao();
            String createdAt     = this.necessidadeList.get(position).getCreatedAt();

            holder.textViewTitulo.setText( titulo );
            holder.textViewJustificativa.setText( justificativa );
            holder.textViewCreatedAt.setText( createdAt );
            holder.textViewAguardando.setText( this.calcularPeriodoAguardandoDonativo(createdAt) );
        }

        @Override
        public int getItemCount() {
            return this.necessidadeList.size();
        }

        public void setMovieList(List<Necessidade> necessidadeList) {
            this.necessidadeList = necessidadeList;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private String calcularPeriodoAguardandoDonativo(String createdAt){

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            LocalDate dtAtual = LocalDate.now();
            LocalDate dtCadastro = LocalDate.parse( createdAt, formatter );

            String dias  = Long.toString( ChronoUnit.DAYS.between(dtCadastro, dtAtual) );
            String meses = Long.toString( ChronoUnit.MONTHS.between(dtCadastro, dtAtual) );
            String anos  = Long.toString( ChronoUnit.YEARS.between(dtCadastro, dtAtual) );

            return anos + " anos, "+meses+" meses e "+dias+" dias";
        }
    }
}
