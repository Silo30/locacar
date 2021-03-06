package inmotion.sistema.uninassau.pe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import inmotion.sistema.uninassau.pe.move.R;

public class main_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            //adicionar o fragmento inicial
            getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, new dashboardFragment()).commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pontinhos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //Menu pontilhado
        if (id == R.id.action_creditos) {
            Intent intent = new Intent(this, activityCreditos.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        //Menu lateral
        if (id == R.id.nav_inicio) {
            getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, new dashboardFragment()).commit();
        } else if (id == R.id.nav_servicos) {
            getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, new listEventoFragment()).commit();
        } else if (id == R.id.nav_estatisticas) {
            getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, new relatoriosFragment()).commit();
        } else if (id == R.id.nav_transportes) {
            getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, new listMeioDeTransporteFragment()).commit();
        }  else if (id == R.id.nav_contatos) {
            getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, new contatoFragment()).commit();
        }else if (id == R.id.nav_localizacao) {
            getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, new localizacaoFragment()).commit();
        }
        else if (id == R.id.nav_sair) {
            finishAndRemoveTask();
            System.exit(0);
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

}
