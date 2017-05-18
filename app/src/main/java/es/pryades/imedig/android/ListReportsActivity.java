package es.pryades.imedig.android;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import es.pryades.imedig.android.utils.DetalleInforme;
import es.pryades.imedig.android.utils.InformeQuery;
import es.pryades.imedig.android.utils.Utils;

public class ListReportsActivity extends MyBaseActivity
{
    private final static String TAG = ListReportsActivity.class.getSimpleName();

    private View mContainerView;
    private View mProgressView;

    private Button infoButton;
    private EditText patientView;

    private ArrayAdapter<DetalleInforme> adapter;
    private ListView listView;
    private List<DetalleInforme> informes;
    private DetalleInforme selectedReport;
    private QueryReportsTask queryTask = null;
    private RetrieveTask retrieveTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_reports);

        mProgressView = findViewById(R.id.query_progress);
        mContainerView = findViewById(R.id.reports_list);

        patientView = (EditText) findViewById(R.id.patient_query);

        listView = (ListView) findViewById(R.id.list);
        listView.setChoiceMode( ListView.CHOICE_MODE_SINGLE );
        listView.setSelector(R.color.selection);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                selectedReport = (DetalleInforme)listView.getItemAtPosition( position );

                infoButton.setVisibility(selectedReport != null ? View.VISIBLE : View.GONE);
            }
        });

        infoButton = (Button) findViewById(R.id.info);
        infoButton.setVisibility(View.GONE); //setEnabled( false );
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onInfoClick();
            }
        });

        Button button = (Button) findViewById( R.id.button_query );
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onQuery();
            }
        });
    }

    private void onCancelClick()
    {
        onBackPressed();
    }

    private void onQuery()
    {
        if ( !patientView.getText().toString().isEmpty() ) {
            showProgress(true, mContainerView, mProgressView);

            selectedReport = null;

            InformeQuery query = new InformeQuery();
            query.setEstado(3);
            query.setPaciente_id(patientView.getText().toString());

            queryTask = new QueryReportsTask(query);
            queryTask.execute((Void) null);
        }
        else {
            AlertDialog alertDialog = new AlertDialog.Builder(ListReportsActivity.this).create();
            alertDialog.setTitle(getString( R.string.failure));
            alertDialog.setMessage( getString( R.string.error_patient) );
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }

    private void onInfoClick() {
        if (selectedReport != null) {

            retrieveTask = new RetrieveTask(selectedReport);
            retrieveTask.execute((Void) null);
        }
    }

    public class QueryReportsTask extends AsyncTask<Void, Void, Boolean>
    {
        private InformeQuery query;

        public QueryReportsTask(InformeQuery query)
        {
            this.query = query;
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences( ListReportsActivity.this );
            String hospitalUrl = sharedPref.getString( SettingsFragment.SETTINGS_IMEDIG_URL, "http://192.168.1.253");
            Integer timeout = Integer.parseInt(sharedPref.getString(SettingsFragment.SETTINGS_IMEDIG_TIMEOUT, "10000"));

            informes = Utils.getInformes( hospitalUrl,query, timeout );

            return true;
        }

        @Override
        protected void onPostExecute( final Boolean success )
        {
            queryTask = null;
            showProgress(false, mContainerView, mProgressView);

            if ( success )
            {
                adapter = new ArrayAdapter<DetalleInforme>( ListReportsActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, informes );
                listView.setAdapter(adapter);

                adapter.notifyDataSetChanged();

                if ( informes.size() == 0 )
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(ListReportsActivity.this).create();
                    alertDialog.setTitle(getString( R.string.failure));
                    alertDialog.setMessage(getString( R.string.reports_not_found));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(ListReportsActivity.this).create();
                alertDialog.setTitle(getString( R.string.failure));
                alertDialog.setMessage(getString( R.string.error_query_reports));
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        }

        @Override
        protected void onCancelled()
        {
            queryTask = null;
            showProgress(false, mContainerView, mProgressView);
        }
    }

    public class RetrieveTask extends AsyncTask<Void, Void, Boolean> {
        private DetalleInforme report;
        private File pdfFile;

        public RetrieveTask( DetalleInforme report )
        {
            this.report = report;
        }

        public boolean isExternalStorageWritable() {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                return true;
            }
            return false;
        }

        public boolean isExternalStorageReadable() {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state) ||
                    Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                return true;
            }
            return false;
        }

        public File getAlbumStorageDir(String albumName) {
            // Get the directory for the user's public pictures directory.
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), albumName);
            if (!file.mkdirs()) {
                Log.e(TAG, "Directory not created");
            }
            return file;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences( ListReportsActivity.this );
            String hospitalUrl = sharedPref.getString( SettingsFragment.SETTINGS_IMEDIG_URL, "http://192.168.1.253");
            Integer timeout = Integer.parseInt(sharedPref.getString(SettingsFragment.SETTINGS_IMEDIG_TIMEOUT, "10000"));

            ByteArrayOutputStream os = Utils.getInforme( hospitalUrl, report.getId(), timeout );

            if (isExternalStorageWritable())
            {
                pdfFile = new File(getExternalFilesDir(null), report.getId().toString() + ".pdf");

                //Log.i(TAG, "pdfFile "+ pdfFile.getAbsolutePath());

                try {
                    FileOutputStream fileOutput = new FileOutputStream(pdfFile);
                    fileOutput.write( os.toByteArray() );
                    fileOutput.flush();;
                    fileOutput.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Log.i(TAG, "******* File not found. Did you add a WRITE_EXTERNAL_STORAGE permission to the manifest?");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return true;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(pdfFile), "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(ListReportsActivity.this).create();
                alertDialog.setTitle(getString( R.string.failure));
                alertDialog.setMessage(getString( R.string.error_query_reports));
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        }
    }
}
