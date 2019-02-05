# README #

### Detailed of class and function are proveide here >>> ###
https://docs.google.com/drawings/d/1F2i14annODXmTCUhoCoIF_sfoBL5rnMRG78ZV5Z53qw/edit

### HOW TO ###

* copy these files to your java folder.
     * EventHelper.java
     * DBHelper.java
     * Showevent.java
     * Event.java
     * Member.java
     * GeneralHelper.java


In build.gradle (Module:app) add this to Dependencies
```
    compile group: 'com.googlecode.json-simple', name: 'json-simple', version: '1.1'
```
In in top of each script, change the name of package regarding your project.
```
package grouptransactionapp.sqllite;
```
In activity page, add onDestroy function to close the connection to Database.
```
    public void onDestroy() {
        super.onDestroy();
        DBHelper db = new DBHelper(this);
        db.close();
    }
```
Call the class by following script
```
final EventHelper db = new EventHelper(this);
final memberHelper mdb = new memberHelper(this);
final TransHelper mdb = new TransHelper(this); // not done yet
```
Call function via class
```
//example

// add Event
db.addEvent("MissC",2,"dine",1,"");
// search member
ArrayList<Member> members ;
members = mdb.searchMember("A");
// sort
db.sortShowEvent(events,sortID_dir)
```

### EVENT EXAMPLE ###
```
// ADD
     EventHelper db = new EventHelper(this);
     ArrayList<Member> members = new ArrayList<Member>();
     // member (String name,int status), note that member_id is not necessary to add, since      it is autoincrement
     members.add(new Member("EE",0));
     members.add(new Member("FF",0));
     members.add(new Member("GG",0));

     // wrap method in if whether to check that addEvent succeed or not.
     // addEvent(String name, int status,int type,int pool, ArrayList<Member> member) ,                          timestamp will automatically be the current time. 
     int check = db.addEvent("Dinner with mom",2,1,1,members);
     if(check >= 0) {
          System.out.print("done");
     } else {
          System.out.print("member's names are repeated");
     }


//SEARCH
     // events should be private variable
     private ArrayList<Showevent> events;
     // get all events
     events = db.searchEvent();
     // search event by name
     String sWord = sometext.toString();
     events =db.searchEvent(sWord);

//SORT 
     private int sortID_dir = 0;
     // sortID_dir should be private variable
     // sortShowEvent(ArrayList<Showevent> event, int dir(0,1), int 
     condition(0=alphabetically,1=chronologically))
     db.sortShowEvent(events,sortID_dir,0);

// CLEAR
     db.clearALLEvent();

```