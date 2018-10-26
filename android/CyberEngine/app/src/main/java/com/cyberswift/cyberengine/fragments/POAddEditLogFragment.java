package com.cyberswift.cyberengine.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cyberswift.cyberengine.R;
import com.cyberswift.cyberengine.activities.BaseActivity;
import com.cyberswift.cyberengine.adapters.POEntryListAdapter;
import com.cyberswift.cyberengine.adapters.ProjectLogDetailsAdapter;
import com.cyberswift.cyberengine.helper.DropDownViewForXML;
import com.cyberswift.cyberengine.listeners.CheckInCheckOutCallBack;
import com.cyberswift.cyberengine.listeners.ServerResponseCallback;
import com.cyberswift.cyberengine.models.POWorkingDaysList;
import com.cyberswift.cyberengine.models.ProjectList;
import com.cyberswift.cyberengine.models.ProjectLogDetails;
import com.cyberswift.cyberengine.models.SubProjectList;
import com.cyberswift.cyberengine.models.SubProjectLogDetails;
import com.cyberswift.cyberengine.services.VolleyTaskManager;
import com.cyberswift.cyberengine.utility.AppConstants;
import com.cyberswift.cyberengine.utility.Constants;
import com.cyberswift.cyberengine.utility.Prefs;
import com.cyberswift.cyberengine.utility.WebServiceConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("ValidFragment")
public class POAddEditLogFragment extends BaseFragment implements CheckInCheckOutCallBack {

    private Context mContext;
    private VolleyTaskManager volleyTaskManager;
    private Prefs mPrefs;
    private ImageView iv_no_data_found;
    private RecyclerView rvLogDetails;
//    private POEntryListAdapter pOEntryListAdapter;
    private String date, day;
    private String currProjectId = "", currProjectName = "", currSubProjectId = "", currSubProjectName = "";
    private int currProjectPosition = 0;
    private TextView tv_date_day;
    private Button btnSave, btnCancel;
    private DropDownViewForXML dropdown_project, dropdown_subproject;
    private ArrayList<ProjectList> arrList_Project;
    private ArrayList<ProjectLogDetails> arrList_ProjectLog = new ArrayList<>();
    private ArrayList<SubProjectLogDetails> arrList_SubProjectLog = new ArrayList<>();
    private HashMap<String, ArrayList<SubProjectList>> hMap = new HashMap<>();
    private ProjectLogDetailsAdapter sPLDAdapter;
    View vi;


    public POAddEditLogFragment(Context mContext, String date, String day) {
        this.mContext = mContext;
        this.date = date;
        this.day = day;
        volleyTaskManager = new VolleyTaskManager(mContext);
        mPrefs = new Prefs(mContext);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_po_addedit_log, container, false);
        mContext = getActivity();
        volleyTaskManager = new VolleyTaskManager(mContext);
        mPrefs = new Prefs(mContext);
        initView(v);
        vi = v;

        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
        tv_date_day.setText(date + "  |  " + day);
        fetchProjectsAndSubprojectsList();
        fetchProjectsLogDetails();
        clickEvents();
    }


    private void initView(View v) {
        iv_no_data_found = v.findViewById(R.id.iv_no_data_found);
        tv_date_day = v.findViewById(R.id.tv_date_day);
        rvLogDetails = v.findViewById(R.id.rvLogDetails);
        btnSave = v.findViewById(R.id.btnSave);
        btnCancel = v.findViewById(R.id.btnCancel);
        dropdown_project = v.findViewById(R.id.dropdown_project);
        dropdown_subproject = v.findViewById(R.id.dropdown_subproject);

        rvLogDetails.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        rvLogDetails.setItemAnimator(new DefaultItemAnimator());

        dropdown_project.setTag("");
        dropdown_subproject.setTag("");

        dropdown_subproject.setText(Constants.SUB_PROJECT_NOT_AVAILABLE);

        if (arrList_ProjectLog.size() > 0) {
            rvLogDetails.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.VISIBLE);
        } else {
            rvLogDetails.setVisibility(View.GONE);
            btnSave.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
        }
    }


    private void fetchProjectsAndSubprojectsList() {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(WebServiceConstants.PARAM_USER_ID, mPrefs.getUserId());
        paramsMap.put(WebServiceConstants.PARAM_PROJECT_NAME, "");
        paramsMap.put(WebServiceConstants.PARAM_LOG_DATE, date);

        volleyTaskManager.getProjectAndSubProjectList(new JSONObject(paramsMap), true, new ServerResponseCallback() {
            @Override
            public void onSuccess(JSONObject resultJsonObject) {
                if (resultJsonObject.optBoolean(WebServiceConstants.RES_PARAM_STATUS)) {
                    arrList_Project = new ArrayList<>();

                    JSONArray jArr_ProjList = resultJsonObject.optJSONObject(WebServiceConstants.RES_PARAM_RESULT).optJSONArray("projects");
                    if (jArr_ProjList != null && jArr_ProjList.length() > 0) {
                        for (int i = 0; i < jArr_ProjList.length(); i++) {
                            ArrayList<SubProjectList> arrList_SubProject = new ArrayList<>();
                            JSONObject jObj = jArr_ProjList.optJSONObject(i);
                            ProjectList sModel = new ProjectList();
                            sModel.setProjectId(jObj.optString("project_id"));
                            sModel.setProjectName(jObj.optString("project_name"));
                            sModel.setProjectType(jObj.optString("project_type"));
                            sModel.setStatusEnd(jObj.optString("status_end"));
                            JSONArray jArrChild = jObj.optJSONArray("sub_projects");
                            for (int j = 0; j < jArrChild.length(); j++) {
                                SubProjectList sSModel = new SubProjectList();
                                sSModel.setEdit(jArrChild.optJSONObject(j).optString("EDIT"));
                                sSModel.setProjectId(jArrChild.optJSONObject(j).optString("project_id"));
                                sSModel.setSubProject(jArrChild.optJSONObject(j).optString("sub_project"));
                                sSModel.setSubProjectCode(jArrChild.optJSONObject(j).optString("sub_project_code"));
                                sSModel.setSubProjectName(jArrChild.optJSONObject(j).optString("sub_project_name"));
                                sSModel.setSubProjectId(jArrChild.optJSONObject(j).optString("sub_projectid"));
                                sSModel.setUserId(jArrChild.optJSONObject(j).optString("user_id"));
                                arrList_SubProject.add(sSModel);
                            }
                            sModel.setSubProjectList(arrList_SubProject);
                            arrList_Project.add(sModel);
                            hMap.put(jObj.optString("project_id"), arrList_SubProject);
                        }
                        prepareProjectDropDownList(arrList_Project);

//                        iv_no_data_found.setVisibility(View.GONE);
//                        setAttendanceListAdapter(arrList_WorkingDays);
                    } /*else
                        iv_no_data_found.setVisibility(View.VISIBLE);*/
                } else {
                    if (resultJsonObject.optInt(WebServiceConstants.RES_PARAM_ERROR_CODE) == AppConstants.ERROR_CODE_AUTHENTICATION_FAILURE) {
                        ((BaseActivity) getActivity()).logout();
                        return;
                    }
                    String msg = "Unexpected error. Please try again.";
                    if (resultJsonObject.opt(WebServiceConstants.RES_PARAM_ERROR_MESSAGE) != null)
                        msg = resultJsonObject.optString(WebServiceConstants.RES_PARAM_ERROR_MESSAGE);

                    iv_no_data_found.setVisibility(View.VISIBLE);
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError() {}
        });

    }


    private void fetchProjectsLogDetails() {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(WebServiceConstants.PARAM_USER_ID, /*"10044"*/mPrefs.getUserId());
        paramsMap.put(WebServiceConstants.PARAM_LOG_DATE, /*"10/04/2018"*/date);

        volleyTaskManager.getProjectLogDetails(new JSONObject(paramsMap), true, new ServerResponseCallback() {
            @Override
            public void onSuccess(JSONObject resultJsonObject) {
                if (resultJsonObject.optBoolean(WebServiceConstants.RES_PARAM_STATUS)) {
                    Log.d("@@@ ", "$$$==> " + resultJsonObject);
                    JSONArray jArr = resultJsonObject.optJSONObject("result").optJSONArray("projects");
                    if (jArr != null && jArr.length() > 0) {
                        for (int i = 0; i < jArr.length(); i++) {
                            ProjectLogDetails sPLDetails = new ProjectLogDetails();
                            sPLDetails.setProjectId(jArr.optJSONObject(i).optString("projectid"));
                            sPLDetails.setProjectName(jArr.optJSONObject(i).optString("project_name"));

                            JSONArray jArrChild = jArr.optJSONObject(i).optJSONArray("Subproject_logs");
                            ArrayList<SubProjectLogDetails> arrListSubProject = new ArrayList<>();
                            for (int j = 0; j < jArrChild.length(); j++) {
                                SubProjectLogDetails sSPLDetails = new SubProjectLogDetails();
                                sSPLDetails.setSubProjectId(jArrChild.optJSONObject(j).optString("sub_project_id"));
                                sSPLDetails.setSubProjectName(jArrChild.optJSONObject(j).optString("sub_project_name"));
                                sSPLDetails.setTotalLoggedHours(jArrChild.optJSONObject(j).optString("hours"));
                                sSPLDetails.setBillableHours(jArrChild.optJSONObject(j).optString("billable_hours"));
                                sSPLDetails.setDescription(jArrChild.optJSONObject(j).optString("description"));

                                sSPLDetails.setApprovedHours(jArrChild.optJSONObject(j).optString("approved_hours"));
                                sSPLDetails.setCreatedById(jArrChild.optJSONObject(j).optString("created_by_id"));
                                sSPLDetails.setEmpDeptId(jArrChild.optJSONObject(j).optString("emp_dept_id"));
                                sSPLDetails.setId(jArrChild.optJSONObject(j).optString("id"));
                                sSPLDetails.setProjectDeptId(jArrChild.optJSONObject(j).optString("project_dept_id"));
                                sSPLDetails.setProjectId(jArrChild.optJSONObject(j).optString("project_id"));
                                sSPLDetails.setProjectMemberId(jArrChild.optJSONObject(j).optString("project_member_id"));
                                sSPLDetails.setProjectName(jArrChild.optJSONObject(j).optString("project_name"));
                                sSPLDetails.setProjectType(jArrChild.optJSONObject(j).optString("project_type"));
                                sSPLDetails.setRejectedHours(jArrChild.optJSONObject(j).optString("rejected_hours"));

                                arrListSubProject.add(sSPLDetails);
                            }
                            sPLDetails.setLogSubDetails(arrListSubProject);
                            arrList_ProjectLog.add(sPLDetails);
                        }
                        setProjectLogDetailsAdapter();
                    }
                } else {
                    if (resultJsonObject.optInt(WebServiceConstants.RES_PARAM_ERROR_CODE) == AppConstants.ERROR_CODE_AUTHENTICATION_FAILURE) {
                        ((BaseActivity) getActivity()).logout();
                        return;
                    }
                    String msg = "Unexpected error. Please try again.";
                    if (resultJsonObject.opt(WebServiceConstants.RES_PARAM_ERROR_MESSAGE) != null)
                        msg = resultJsonObject.optString(WebServiceConstants.RES_PARAM_ERROR_MESSAGE);

                    iv_no_data_found.setVisibility(View.VISIBLE);
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError() {}
        });

    }


    // set array list to related to dropdown
    private void prepareProjectDropDownList(ArrayList<ProjectList> arrList_Project) {
        if (arrList_Project.size() > 0) {
            String[] projArr = new String[arrList_Project.size()];
            for (int i = 0; i < arrList_Project.size(); i++) {
                projArr[i] = arrList_Project.get(i).getProjectName();
            }
            dropdown_project.setItems(projArr);
            dropdown_project.setText(Constants.SELECT_PROJECT);
        } else {
            dropdown_project.setText(Constants.PROJECT_NOT_AVAILABLE);
        }
    }


    // set array list to related to dropdown
    private void prepareSubProjectDropDownList(ArrayList<SubProjectList> arrList_SubProject) {
        if (arrList_SubProject.size() > 0) {
            String[] sProjArr = new String[arrList_SubProject.size()];
            for (int i = 0; i < arrList_SubProject.size(); i++) {
                sProjArr[i] = arrList_SubProject.get(i).getSubProjectName();
            }
            dropdown_subproject.setItems(sProjArr);
            dropdown_subproject.setText(Constants.SELECT_SUB_PROJECT);
        } else {
            dropdown_subproject.setText(Constants.SUB_PROJECT_NOT_AVAILABLE);
        }
    }


    // multiple views click work
    private void clickEvents() {

        // project dropdown click
        dropdown_project.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dropdown_project.setTag(arrList_Project.get(i).getProjectId());
                currProjectId = arrList_Project.get(i).getProjectId();
                currProjectName = arrList_Project.get(i).getProjectName();
                currProjectPosition = i;
                // if project has any subproject then load subproject list
                if (arrList_Project.get(i).getSubProjectList().size() > 0) {
                    prepareSubProjectDropDownList(arrList_Project.get(i).getSubProjectList());
                }
            }
        });


        // sub project dropdown click
        dropdown_subproject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currSubProjectId = hMap.get(currProjectId).get(i).getSubProjectId();
                currSubProjectName = hMap.get(currProjectId).get(i).getSubProjectName();
                checkProjectAndSubProjectIsAvailableOrNot();
                rvLogDetails.setVisibility(View.VISIBLE);
                btnSave.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.VISIBLE);
            }
        });


        // on save button click
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < rvLogDetails.getChildCount(); i++) {
                    RecyclerView.ViewHolder holder = rvLogDetails.findViewHolderForAdapterPosition(i);
                    TextView tv = holder.itemView.findViewById(R.id.tvProjectName);
                    RecyclerView rv = holder.itemView.findViewById(R.id.rvSubProjectLogDetails);
                    Log.d("@@@ ", "Project name==> " + tv.getText().toString());
                    for (int j = 0; j < rv.getChildCount(); j++) {
                        RecyclerView.ViewHolder holderChild = rv.findViewHolderForAdapterPosition(j);
                        TextView tv1 = holderChild.itemView.findViewById(R.id.tvSubProjectName);
                        EditText etTotalHours = holderChild.itemView.findViewById(R.id.etTotalLoggedHour);
                        EditText etBillingHours = holderChild.itemView.findViewById(R.id.etBilledHour);
                        EditText etDesc = holderChild.itemView.findViewById(R.id.et_log_desc);

                        String subProjectName = tv1.getText().toString();
                        String description = etDesc.getText().toString();
                        String totalLoggedHours = etTotalHours.getText().toString();
                        String billableHours = etBillingHours.getText().toString();

                        Log.d("@@@ ", "Sub project name==> " + subProjectName
                                + "\nDesc==> " + description
                                + "\ntotalLoggedHours==> " + totalLoggedHours
                                + "\nbillableHours==> " + billableHours
                        );

                        TextView tvSubProjectId = holderChild.itemView.findViewById(R.id.tvSubProjectId);
                        TextView tvApprovedHours = holderChild.itemView.findViewById(R.id.tvApprovedHours);
                        TextView tvRejectedHours = holderChild.itemView.findViewById(R.id.tvRejectedHours);
                        TextView tvCreatedById = holderChild.itemView.findViewById(R.id.tvCreatedById);
                        TextView tvEmpDeptId = holderChild.itemView.findViewById(R.id.tvEmpDeptId);
                        TextView tvId = holderChild.itemView.findViewById(R.id.tvId);
                        TextView tvProjectDeptId = holderChild.itemView.findViewById(R.id.tvProjectDeptId);
                        TextView tvProjectId = holderChild.itemView.findViewById(R.id.tvProjectId);
                        TextView tvProjectMemberId = holderChild.itemView.findViewById(R.id.tvProjectMemberId);
                        TextView tvProjectName = holderChild.itemView.findViewById(R.id.tvProjectName);
                        TextView tvProjectType = holderChild.itemView.findViewById(R.id.tvProjectType);

                        String subProjectId = tvSubProjectId.getText().toString();
                        String approvedHours = tvApprovedHours.getText().toString();
                        String rejectedHours = tvRejectedHours.getText().toString();
                        String createdById = tvCreatedById.getText().toString();
                        String empDeptId = tvEmpDeptId.getText().toString();
                        String id = tvId.getText().toString();
                        String projectDeptId = tvProjectDeptId.getText().toString();
                        String projectId = tvProjectId.getText().toString();
                        String projectMemberId = tvProjectMemberId.getText().toString();
                        String projectName = tvProjectName.getText().toString();
                        String projectType = tvProjectType.getText().toString();

                        Log.d("@@@ ", "subProjectId==> " + subProjectId
                                + "\napprovedHours==> " + approvedHours
                                + "\nrejectedHours==> " + rejectedHours
                                + "\ncreatedById==> " + createdById
                                + "\nempDeptId==> " + empDeptId
                                + "\nid==> " + id
                                + "\nprojectDeptId==> " + projectDeptId
                                + "\nprojectId==> " + projectId
                                + "\nprojectMemberId==> " + projectMemberId
                                + "\nprojectName==> " + projectName
                                + "\nprojectType==> " + projectType
                        );
                    }
                }
            }
        });


        // on cancel button click
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Cancel!", Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void checkProjectAndSubProjectIsAvailableOrNot() {
        if (arrList_ProjectLog.size() > 0) {
            // if list has any value then
            // first check selected project is in list or not
            // if available then check sub project is available or not
            // if sub project is available then do nothing else add a new sub project
            // if project is not abailable then add a project with its selected sub project
            if (isProjectAvailable()) {
                if (isSubProjectAvailable()) {
                    Toast.makeText(mContext, "Already added.", Toast.LENGTH_SHORT).show();
                } else {
                    // insert a new sub project within selected project
                    insertSubProjectToExistingProject();
                }
            } else {
                insertNewProjectAndSubProject();
            }

        } else {
            insertNewProjectAndSubProject();
        }
    }


    // check selected project available in list or not
    private Boolean isProjectAvailable() {
        Boolean status = false;
        for (int i = 0; i < arrList_ProjectLog.size(); i++) {
            if (arrList_ProjectLog.get(i).getProjectId().equals(currProjectId)) {
                // store selected project's subproject list in arraylist - used to check subproject available or not later
                arrList_SubProjectLog.clear();
                arrList_SubProjectLog.addAll(arrList_ProjectLog.get(i).getLogSubDetails());
                return true; // return if found project in list
            } else
                status = false;
        }
        return status;
    }


    // check selected sub project available in list or not
    private Boolean isSubProjectAvailable() {
        Boolean status = false;
        for (int i = 0; i < arrList_SubProjectLog.size(); i++) {
            if (arrList_SubProjectLog.get(i).getSubProjectId().equals(currSubProjectId)) {
                return true;
            } else
                status = false;
        }
        return status;
    }


    // insert a new project and sub project to list and set adapter
    private void insertNewProjectAndSubProject() {
        ProjectLogDetails sPLDetails = new ProjectLogDetails();
        sPLDetails.setProjectId(currProjectId);
        sPLDetails.setProjectName(currProjectName);
        ArrayList<SubProjectLogDetails> arrListSubProject = new ArrayList<>();
        SubProjectLogDetails sSPLDetails = new SubProjectLogDetails();
        sSPLDetails.setSubProjectId(currSubProjectId);
        sSPLDetails.setSubProjectName(currSubProjectName);
        sSPLDetails.setTotalLoggedHours("0.0");
        sSPLDetails.setBillableHours("0.0");
        sSPLDetails.setDescription("");
        arrListSubProject.add(sSPLDetails);
        sPLDetails.setLogSubDetails(arrListSubProject);
        arrList_ProjectLog.add(sPLDetails);
        setProjectLogDetailsAdapter();
    }


    // insert a sub project to existing project list and set adapter
    private void insertSubProjectToExistingProject() {
        SubProjectLogDetails sSPLDetails = new SubProjectLogDetails();
        sSPLDetails.setSubProjectId(currSubProjectId);
        sSPLDetails.setSubProjectName(currSubProjectName);
        sSPLDetails.setTotalLoggedHours("0.0");
        sSPLDetails.setBillableHours("0.0");
        sSPLDetails.setDescription("");
        arrList_ProjectLog.get(currProjectPosition).getLogSubDetails().add(sSPLDetails);
        setProjectLogDetailsAdapter();
    }


    // set adapter
    private void setProjectLogDetailsAdapter() {
        sPLDAdapter = new ProjectLogDetailsAdapter(mContext, arrList_ProjectLog);
        rvLogDetails.setAdapter(sPLDAdapter);
        sPLDAdapter.notifyDataSetChanged();

        rvLogDetails.setVisibility(View.VISIBLE);
        btnSave.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.VISIBLE);
    }


    @Override
    public void onCheckInStatusChange() {
        ((BaseActivity) mContext).getSupportFragmentManager()
                .beginTransaction()
                .detach(this)
                .attach(this)
                .commit();
    }

}
