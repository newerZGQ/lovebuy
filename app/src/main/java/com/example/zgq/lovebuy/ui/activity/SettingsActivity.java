package com.example.zgq.lovebuy.ui.activity;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.zgq.lovebuy.R;
import com.example.zgq.lovebuy.data.ConsumContentProvider;
import com.example.zgq.lovebuy.data.DesireContentProvider;
import com.example.zgq.lovebuy.data.SharedPreferenceData;
import com.example.zgq.lovebuy.model.user.MyUser;
import com.example.zgq.lovebuy.util.DBOperateTools;
import com.example.zgq.lovebuy.util.MyActivityManager;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Button exitBtn;
    private TextView statusbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyActivityManager.addActivity(this);
        setContentView(R.layout.activity_settings);
        statusbar = (TextView) findViewById(R.id.status_bar);
        if (Build.VERSION.SDK_INT<21){
            statusbar.setVisibility(View.GONE);
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        exitBtn = (Button) findViewById(R.id.exit_button);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setMessage(R.string.will_delete_all_data).setTitle(R.string.confirm_exit)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // FIRE ZE MISSILES!
                                BmobUser.logOut(SettingsActivity.this);   //clear user cache
                                DBOperateTools.clearConsum(SettingsActivity.this, Uri.parse(ConsumContentProvider.URI + "/consum"));
                                DBOperateTools.clearDesire(SettingsActivity.this, Uri.parse(DesireContentProvider.URI + "/desire"));
                                MyActivityManager.finishAllActivities();
                                startActivity(new Intent(SettingsActivity.this, LaunchActivity.class));
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog

                            }
                        }).show();

            }
        });
        getFragmentManager().beginTransaction().replace(R.id.preference_fragment, new MyPreferenceFragment(SettingsActivity.this)).commit();
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyActivityManager.removeActivity(this);
    }

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else if (preference instanceof RingtonePreference) {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_ringtone_silent);

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null);
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    public static class MyPreferenceFragment extends PreferenceFragment {
        private Context context;
        private Preference myAcountPref;
        private Preference associatedPref;
        private Preference disAssociatePref;
        private Preference aboutApp;


        public MyPreferenceFragment() {
        }

        public MyPreferenceFragment(Context context) {
            this.context = context;
        }


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_acount);

            myAcountPref = (Preference) findPreference(getResources().getString(R.string.my_count_pref_key));
            SpannableStringBuilder myAcount = new SpannableStringBuilder(getResources().getString(R.string.my_count) + "  " + BmobUser.getCurrentUser(context).getUsername());
            myAcount.setSpan(new ForegroundColorSpan(Color.GRAY), 6, myAcount.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            myAcountPref.setTitle(myAcount);

            associatedPref = (Preference) findPreference(getResources().getString(R.string.associated_count_pref_key));
            setAssociatedAcount();
            associatedPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    if (SharedPreferenceData.getSharedPreferenceDataAssociateUserName(context).equals(getString(R.string.have_not_associat_with_other))) {
                        startActivity(new Intent(context, AssociateActivity.class));
                    } else {
                        Toast.makeText(context, getString(R.string.have_associated_a_acount), Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            });
            disAssociatePref = (Preference) findPreference(getResources().getString(R.string.disassociate_acount_key));
            disAssociatePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    if ((BmobUser.getCurrentUser(context).getObjectByKey(context, "associateEmail").toString()).equals(getResources().getString(R.string.have_not_associate))) {
                        Toast.makeText(context, getString(R.string.have_not_associat_with_other), Toast.LENGTH_SHORT).show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage(R.string.confirm_disassociate)
                                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // FIRE ZE MISSILES!
                                        MyUser user = BmobUser.getCurrentUser(context, MyUser.class);
                                        MyUser newUser = new MyUser();
                                        newUser.setAssociateEmail(getResources().getString(R.string.have_not_associate));
                                        newUser.update(context, user.getObjectId(), new UpdateListener() {
                                            @Override
                                            public void onSuccess() {
                                                Toast.makeText(context, getString(R.string.disassociated_succesful), Toast.LENGTH_SHORT).show();
                                                SharedPreferenceData.deleteSharedPreferenceDataAssociateUserName(context);
                                                setAssociatedAcount();
                                            }

                                            @Override
                                            public void onFailure(int i, String s) {
                                                Toast.makeText(context, getString(R.string.disassociated_failed), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                })
                                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User cancelled the dialog

                                    }
                                }).show();

                    }
                    return true;
                }
            });
            aboutApp = (Preference)findPreference(getResources().getString(R.string.about_app_pref_key));
            aboutApp.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(getString(R.string.about_app_dialog_title)).setMessage("Bug或建议请邮件至379020727@qq.com\n\nQQ交流群  519142680\n\n作者  亥姆霍兹").show();
                    return true;
                }
            });
        }

        @Override
        public void onResume() {
            super.onResume();
            setAssociatedAcount();
        }
        public void setAssociatedAcount(){
            String s = SharedPreferenceData.getSharedPreferenceDataAssociateUserName(context);
            SpannableStringBuilder associatedAcount = new SpannableStringBuilder(getResources().getString(R.string.associated_count) + "  " + s);
            associatedAcount.setSpan(new ForegroundColorSpan(Color.GRAY), 7, associatedAcount.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            associatedPref.setTitle(associatedAcount);
        }
    }
}
