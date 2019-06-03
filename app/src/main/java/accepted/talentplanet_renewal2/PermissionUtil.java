package accepted.talentplanet_renewal2;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Created by kwonhong on 2018-03-13.
 */

public class PermissionUtil {

    public static final int REQUST_LOCATION = 2002;
    public static final String[] PERMISSONS_LOCATION = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    public static boolean checkPermissions(Context context, String permission){
        int permissionResult = ActivityCompat.checkSelfPermission(context, permission);
        if(permissionResult == PackageManager.PERMISSION_GRANTED) return true;
        else return false;
    }

    public static boolean verifyPermission(int[] grantresults){
        if(grantresults.length < 1){
            return false;
        }
        for(int result : grantresults){
            if(result != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }

        return true;
    }
}
