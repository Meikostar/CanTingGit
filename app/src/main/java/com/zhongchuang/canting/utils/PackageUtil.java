package com.zhongchuang.canting.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;


import java.util.List;

/**
 * Created by fl on 2017/4/28.
 */

public class PackageUtil {


    public static List<PackageInfo> getAllPackageName(Context context){
        PackageManager pm = context.getPackageManager();
        return pm.getInstalledPackages(0);

    }

    public static List<ApplicationInfo> getAllApplications(Context context){
        PackageManager pm = context.getPackageManager();
        return pm.getInstalledApplications(0);

    }

    public static List<ResolveInfo> getAllResolve(Context context){
        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN,null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        return pm.queryIntentActivities(intent,0);


    }

    public static PackageInfo hasPackage(Context context,String packName){

        List<PackageInfo> packageInfos = getAllPackageName(context);
        for (PackageInfo p:packageInfos) {

            if (p.packageName.equals(packName)){
                return p;
            }
        }

        return null;

    }

    public static ResolveInfo hasAppName(Context context,String packName){

        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN,null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> packageInfos = pm.queryIntentActivities(intent,0);

        for (ResolveInfo p:packageInfos) {
//            LogUtil.d("应用的名称="+p.loadLabel(pm));
            if (p.loadLabel(pm).equals(packName)){
                LogUtil.d("应用的名称="+p.loadLabel(pm));
                return p;
            }
        }

        return null;

    }


    public static void luanchApp(Context context,String name){
        PackageManager pm = context.getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(name);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        context.startActivity(intent);
    }

    public static void luanchAppByName(Context context,String name){
        PackageInfo info = hasPackage(context,name);
        if (info==null){
            return;
        }
    }


    public static void uninstallAPK(Context context, String packageName) {
        Uri packageURI = Uri.parse("package:" + packageName);
        Intent uninstallIntent = new Intent("android.intent.action.DELETE", packageURI);
        context.startActivity(uninstallIntent);
    }

}
