package br.com.caelum.cadastro;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import br.com.caelum.cadastro.dao.AlunoDAO;
import br.com.caelum.cadastro.modelo.Aluno;


public class ListaAlunosActivity extends ActionBarActivity {

    public static final String ALUNO_SELECIONADO = "alunoSelecionado";

    private ListView listaAlunos;
    private List<Aluno> alunos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_alunos);

        this.carregaLista();
        registerForContextMenu(this.listaAlunos);

        this.listaAlunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent edicao = new Intent(ListaAlunosActivity.this, FormularioActivity.class);

                Aluno aluno = (Aluno)parent.getItemAtPosition(position);
                edicao.putExtra("aluno", aluno);

                startActivity(edicao);
            }
        });

        Button botaoAdiciona = (Button)findViewById(R.id.lista_alunos_floating_button);

        botaoAdiciona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent formulario = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                startActivity(formulario);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        this.carregaLista();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final Aluno alunoSelecionado = ListaAlunosActivity.this.alunos.get(info.position);

        MenuItem delete = menu.add("Manda pro limbo");
        delete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                AlunoDAO dao = new AlunoDAO(ListaAlunosActivity.this);
                dao.delete(alunoSelecionado);
                dao.close();

                carregaLista();

                return false;
            }
        });
    }

    // Private methods

    private void carregaLista() {
        AlunoDAO dao = new AlunoDAO(this);
        this.alunos = dao.getLista();
        dao.close();

        final ArrayAdapter<Aluno> adapter = new ArrayAdapter<Aluno>(this, android.R.layout.simple_list_item_1, alunos);

        this.listaAlunos = (ListView) findViewById(R.id.lista_alunos);
        this.listaAlunos.setAdapter(adapter);
    }
}