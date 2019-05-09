//package Util.Realm;
//
//import android.app.Activity;
//import android.app.Application;
//import android.app.Fragment;
//
//import Model.UserLocation;
//import io.realm.Realm;
//import io.realm.RealmResults;
//
///**
// * Created by alber on 29/11/2018.
// */
//
//public class RealmController {
//
//    private static RealmController instance;
//    private final Realm realm;
//
//    public RealmController(Application application) {
//        realm = Realm.getDefaultInstance();
//    }
//
//    public static RealmController with(Fragment fragment) {
//
//        if (instance == null) {
//            instance = new RealmController(fragment.getActivity().getApplication());
//        }
//        return instance;
//    }
//
//    public static RealmController with(Activity activity) {
//
//        if (instance == null) {
//            instance = new RealmController(activity.getApplication());
//        }
//        return instance;
//    }
//
//    public static RealmController with(Application application) {
//
//        if (instance == null) {
//            instance = new RealmController(application);
//        }
//        return instance;
//    }
//
//    public static RealmController getInstance() {
//
//        return instance;
//    }
//
//    public Realm getRealm() {
//
//        return realm;
//    }
//
//    //Refresh the realm istance
//    public void refresh() {
//
//        realm.refresh();
//    }
//
//    //clear all objects from Book.class
//    public void clearAll() {
//
//        realm.beginTransaction();
//        realm.clear(UserLocation.class);
//        realm.commitTransaction();
//    }
//
//    //find all objects in the Book.class
//    public RealmResults<UserLocation> getBooks() {
//
//        return realm.where(UserLocation.class).findAll();
//    }
//
//    //query a single item with the given id
//    public UserLocation getBook(String id) {
//
//        return realm.where(UserLocation.class).equalTo("id", id).findFirst();
//    }
//
//    //check if Book.class is empty
//    public boolean hasBooks() {
//
//        return !realm.allObjects(UserLocation.class).isEmpty();
//    }
//
//    //query example
//    public RealmResults<UserLocation> queryedBooks() {
//
//        return realm.where(UserLocation.class)
//                .contains("username", "Author 0")
//                .or()
//                .contains("title", "Realm")
//                .findAll();
//
//    }
//}
