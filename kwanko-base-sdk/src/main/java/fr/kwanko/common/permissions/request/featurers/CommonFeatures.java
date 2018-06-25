package fr.kwanko.common.permissions.request.featurers;

/**
 * Created by erusu on 10/18/2016.
 */

public class CommonFeatures {

    public static final Location Location = new Location();

    public static class Location implements Feature {
        private Location(){}

        @Override
        public String getPermission() {
            return null;
        }
    }
    public static class GenericFeature implements Feature{
        private final String permission;
        public GenericFeature(String permission){
            this.permission = permission;
        }

        public String getPermission(){
            return permission;
        }
    }
}
