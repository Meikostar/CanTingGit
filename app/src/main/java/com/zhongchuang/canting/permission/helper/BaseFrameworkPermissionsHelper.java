package com.zhongchuang.canting.permission.helper;

import android.app.FragmentManager;
import android.support.annotation.NonNull;

import com.zhongchuang.canting.permission.RationaleDialogFragment;


/**
 * Implementation of {@link PermissionHelper} for framework host classes.
 */
public abstract class BaseFrameworkPermissionsHelper<T> extends PermissionHelper<T> {

    public BaseFrameworkPermissionsHelper(@NonNull T host) {
        super(host);
    }

    public abstract FragmentManager getFragmentManager();

    @Override
    public void showRequestPermissionRationale(@NonNull String rationale,
                                               int positiveButton,
                                               int negativeButton,
                                               int requestCode,
                                               @NonNull String... perms) {
        RationaleDialogFragment
                .newInstance(positiveButton, negativeButton, rationale, requestCode, perms)
                .show(getFragmentManager(), RationaleDialogFragment.TAG);
    }
}
