import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;
 
import java.util.ArrayList;
 ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 ////////////////////////////////////////////////////////////// load event command /////////////////////////////////////////////////////
 ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class Event {
    public String name;
    public int id;
    public int start_date;
    public int due_date;
    public boolean group;
    public int[] mem_id;
   // public ArrayList <Integer> mem_id = new ArrayList<Integer>();
   // public ArrayList <Double> mem_bal = new ArrayList<Double>();
    public double[] mem_bal;
   // public ArrayList <Boolean> mem_act = new ArrayList<Boolean>();
    public boolean[] mem_act;
    // create array Transaction object
    public boolean[] mem_fav;
    public ArrayList <Transaction> tran = new ArrayList<Transaction>();
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////// load event command /////////////////////////////////////////////////////
    //default create event
    //normal create new event 
    public Event(String e_name,int e_id,int e_start_date,int e_due_date,boolean g,int[] m_id){
        this.name = e_name;
        this.id = e_id;
        this.start_date = e_start_date;
        this.due_date = e_due_date;
        this.group = g;
        this.mem_id = m_id;
        int si = m_id.length;
        this.mem_bal = new double[si];
        Arrays.fill(mem_bal, 0);
        this.mem_act = new boolean[si] ;
        Arrays.fill(mem_act, true);
        this.mem_fav = new boolean[si] ;
        Arrays.fill(mem_fav, false);
        
        // jsaon
        // member_id,active_id,favourite (json) (string) 
        JSONArray json_obj_array = new JSONArray();
       for(int i =0; i < mem_id.length;i++){
           //obj.put(new Integer(mem_id[i]),new Boolean(true),new Boolean(false));
           JSONObject obj = new JSONObject();
           obj.put("id",new Integer(mem_id[i]));
           obj.put("mem_act",new Boolean(true));
           obj.put("mem_fav",new Boolean(false));
           json_obj_array.add(obj);
        }
        //
        String jsonText_mem = json_obj_array.toString();
        String jsonText_tran = "";
        /*
        database command adding to db
        */
    }
    // load Event
    public Event(int e_id){
        // load database
        // find e_id
        
        // decode json
        // string event_json_mem form database
        String event_json_mem = "event_json_mem";
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(event_json_mem);
        JSONArray json_array_mem = (JSONArray)obj;
        // string event_json_mem form database
        String event_json_tran = "event_json_tran";
        parser = new JSONParser();
        obj = parser.parse(event_json_tran);
        JSONArray json_array_tran = (JSONArray)obj;
        // decode json
        
        // create array form member
        int size_mem = json_array_mem.length();
        this.mem_id = new int[size_mem];
        this.mem_bal = new double[size_mem];
        this.mem_act = new boolean[size_mem];
        this.mem_fav = new boolean[size_mem];
        for(int i = 0; i < size_mem ;i++){
            JSONObject jsonobject = json_obj_array.getJSONObject(i);
            this.mem_id[i] = jsonobject.getInt("id");
            this.mem_act[i] = jsonobject.getBoolean("mem_act");
            this.mem_fav[i] = jsonobject.getBoolean("mem_fav");
        }
        // create array form transaction
        int size_tran = json_array_tran.length();
        for(int i = 0; i < size_tran ;i++){
            JSONObject jsonobject = json_array_tran.getJSONObject(i);
            int id_tran = jsonobject.getInt("id");
            //load db
            
            //add tran
            String type_tran = "type_tran";
            // if cause 
            if(type_tran =="pay"){
            }else if(type_tran == "get"){
            }else if(type_tran == "tranfer"){
            }else{
                System.out.print("error");
            }
        }
        /*
        database command
        */
        
        // update event
        update_bal_all();
    }
    ////////////////////////////////////////////////////////////// setting function in Event  /////////////////////////////////////////////////////
    //add member
    public void add_mem(int m_id){
        //เข้า db 
        
        //ถอด json string
        // decode json
        String event_json_mem = "event_json_mem";
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(event_json_mem);
        JSONArray json_array_mem = (JSONArray)obj;
        //remove data in database
        
        // decode json
        int size = json_array_mem.length();
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        int si = mem_id.length;
        //public static void arraycopy(Object src, int srcPos, Object dest, int destPos, int length)
        int[] mem_id_2 = new int[si];
        System.arraycopy(mem_id_2,0,this.mem_id,0,si);
        this.mem_id  = new int[si+1];
        System.arraycopy(this.mem_id,0,mem_id_2,0,si);
        mem_id[si] = m_id;    
        //mem_id.push(m_id);[si] = m_id;
        double[] mem_bal_2 = new double[si];
        System.arraycopy(mem_bal_2,0,this.mem_bal,0,si);
        this.mem_bal  = new double[si+1];
        System.arraycopy(this.mem_bal,0,mem_bal_2,0,si);
        mem_bal[si] = 0;
        
        boolean[] mem_act_2 = new boolean[si];
        System.arraycopy(mem_act_2,0,this.mem_act,0,si);
        this.mem_act  = new boolean[si+1];
        System.arraycopy(this.mem_act,0,mem_act_2,0,si);
        mem_act[si] = true;
        
        boolean[] mem_fav_2 = new boolean[si];
        System.arraycopy(mem_fav_2,0,this.mem_fav,0,si);
        this.mem_fav  = new boolean[si+1];
        System.arraycopy(this.mem_fav,0,mem_act_2,0,si);
        mem_act[si] = true;
        ////////////////////////////////////////// remove form database


        //json string
           //obj.put(new Integer(mem_id[i]),new Boolean(true),new Boolean(false));
        JSONObject obj2 = new JSONObject();
        obj2.put("id",new Integer(mem_id[i]));
        obj2.put("mem_act",new Boolean(true));
        obj2.put("mem_fav",new Boolean(false));
        json_array_mem.add(obj2);
       //    list_mem_id.add(new Integer(mem_id[i]));
        //
        String jsonText_mem = json_array_mem.toString();
        /*
        database command
        */
    }
    // inactive member
    public void active_mem(int m_id){
        //load database
        
        //ถอด json string
        // decode json
        String event_json_mem = "event_json_mem";
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(event_json_mem);
        JSONArray json_array_mem = (JSONArray)obj;
        int i =0;
        int si = mem_id.length;
        while(i < si) {
            if(mem_id[i] == m_id){
                mem_act[i] = true;
                //replace json
                JSONObject obj_to_change = json_array_mem.getJSONObject(i);
                int get_id = obj_to_change.getInt("id");
                boolean get_act = obj_to_change.getBoolean("mem_act");
                boolean get_fav = obj_to_change.getBoolean("mem_fav");
                //replace value put(int index, Object value)
                JSONObject obj2 = new JSONObject();
                obj2.put("id",new Integer(mem_id[i]));
                obj2.put("mem_act",new Boolean(true));
                obj2.put("mem_fav",new Boolean(get_fav));
                json_array_mem  = put(i,obj2);
                break;
            }
            i++;
        }
        String jsonText = json_array_mem.toString();
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //update database
        
        /*
        database command
        */
    }
    
    public void inactive_mem(int m_id){
        //load database
        
        //ถอด json string
        // decode json
        String event_json_mem = "event_json_mem";
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(event_json_mem);
        JSONArray json_array_mem = (JSONArray)obj;
        int i =0;
        int si = mem_id.length;
        while(i < si) {
            if(mem_id[i] == m_id){
                mem_act[i] = false;
                //replace json
                JSONObject obj_to_change = event_json_mem.getJSONObject(i);
                int get_id = obj_to_change.getInt("id");
                boolean get_act = obj_to_change.getBoolean("mem_act");
                boolean get_fav = obj_to_change.getBoolean("mem_fav");
                //replace value put(int index, Object value)
                JSONObject obj2 = new JSONObject();
                obj2.put("id",new Integer(get_id));
                obj2.put("mem_act",new Boolean(false));
                obj2.put("mem_fav",new Boolean(get_fav));
                event_json_mem  = put(i,obj2);
                break;
            }
            i++;
        }
        // change json to String
        String jsonText = event_json_mem.toString();
        //load json string from database
        
        /*
        database command
        */
    }
    // inactive member
    public void active_fav(int m_id){
        //load database
        
        //ถอด json string
        // decode json
        String event_json_mem = "event_json_mem";
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(event_json_mem);
        JSONArray json_array_mem = (JSONArray)obj;
        
        int i =0;
        int si = mem_id.length;
        while(i < si) {
            if(mem_id[i] == m_id){
                mem_fav[i] = true;
                //replace json
                JSONObject obj_to_change = json_array_mem.getJSONObject(i);
                int get_id = obj_to_change.getInt("id");
                boolean get_act = obj_to_change.getBoolean("mem_act");
                boolean get_fav = obj_to_change.getBoolean("mem_fav");
                //replace value put(int index, Object value)
                JSONObject obj2 = new JSONObject();
                obj2.put("id",new Integer(get_id));
                obj2.put("mem_act",new Boolean(get_act));
                obj2.put("mem_fav",new Boolean(true));
                json_array_mem  = put(i,obj2);
                break;
            }
            i++;
        }
         //
        String jsonText_mem = json_array_mem.toString();     
        /*
        database command
        */
    }
    // inactive member
    public void inactive_fav(int m_id){
        //ถอด json string
        // decode json
        String event_json_mem = "event_json_mem";
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(event_json_mem);
        JSONArray json_array_mem = (JSONArray)obj;
        
        int i =0;
        int si = mem_id.length;
        while(i < si) {
            if(mem_id[i] == m_id){
                mem_fav[i] = false;
                //replace json
                JSONObject obj_to_change = json_array_mem.getJSONObject(i);
                int get_id = obj_to_change.getInt("id");
                boolean get_act = obj_to_change.getBoolean("mem_act");
                boolean get_fav = obj_to_change.getBoolean("mem_fav");
                //replace value put(int index, Object value)
                JSONObject obj2 = new JSONObject();
                obj2.put("id",new Integer(get_id));
                obj2.put("mem_act",new Boolean(get_act));
                obj2.put("mem_fav",new Boolean(false));
                json_array_mem  = put(i,obj2);
                break;
            }
            i++;
        }
        //
        String jsonText_mem = json_array_mem.toString();
        
        
        /*
        database command
        */
    }
    ////////////////////////////////////////////////////////////// function in Event  /////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////// add transaction command //////////////////////////////////////////////////////////////////
    // add paying action 
    public void add_paying(int[] m_id,double money,String name){
        // ดึง id จาก database
        int index_last_tran =0 ;
        // เปลี่ยนเพิ่มเลขใน database
        String tran_event = "Transaction in event";
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(tran_event);
        JSONArray json_array_tran = (JSONArray)obj;
        json_array_tran.add(new Integer(index_last_tran));
        String jsonText_tran = json_array_tran.toString();
        // member_id,active_id,favourite (json) (string) 
        // add เข้า array list
        Paying tran_add = new Paying(index_last_tran,money,name,"pay");
        tran_add.set(m_id);
        tran.add(tran_add);
        mem_bal = tran_add.update(mem_id,mem_bal);
        
        //database
        // ตอนเปลี่ยนเป็น jason creditor (user_id) (json)	accept (user_id) (json)
        // "member in Transaction";
        JSONArray json_array_tran_mem = new JSONArray();
        //JSONArray list_mem_id = new JSONArray();
        for(int i = 0; i< m_id.length;i++){
            json_array_tran_mem.add(new Integer(m_id[i]));
        }
        String jsonText_mem_tran = json_array_tran_mem.toString();
        // เข้า database
        /*
        database command
        */
        
        // update
            int size = tran.size();
        // copy
        /*    Transaction[] tran2 = new Transaction[size];
            System.arraycopy(tran, 0, tran2, 0, size);
            Transaction[] tran = new Transaction[size+1];
            System.arraycopy(tran2, 0, tran, 0, size);
         */
        
          //ดึง index_last_tran เข้า jason
        
    }
    // add accepting action 
    public void add_getting(int[] m_id,double money){
        // ดึง id จาก database
        int index_last_tran =0 ;
        // เปลี่ยนเพิ่มเลขใน database
        String tran_event = "Transaction in event";
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(tran_event);
        JSONArray json_array_tran = (JSONArray)obj;
        json_array_tran.add(new Integer(index_last_tran));
        String jsonText_tran = json_array_tran.toString();
        
        // add เข้า array list
        Paying tran_add = new Paying(index_last_tran,money,name,"pay");
        tran_add.set(m_id);
        tran.add(tran_add);
        mem_bal = tran_add.update(mem_id,mem_bal);
        
        
        // copy array
        //System.arraycopy(arr1, 0, arr2, 0, 1);
        // ตอนเปลี่ยนเป็น jason creditor (user_id) (json)	accept (user_id) (json)
        JSONArray json_array_tran_mem = new JSONArray();
        //JSONArray list_mem_id = new JSONArray();
        for(int i = 0; i< m_id.length;i++){
            json_array_tran_mem.add(new Integer(m_id[i]));
        }
        String jsonText_mem_tran = json_array_tran_mem.toString();
        
        // เข้า database
        /*
        database command
        */
        
        
        //ดึง index_last_tran เข้า jason
    }
    // add tranfering action 
    public void add_tranfering(int[] m_id,double money){
        // ดึง id จาก database
        int index_last_tran =0 ;
        // เปลี่ยนเพิ่มเลขใน database
        String tran_event = "Transaction in event";
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(tran_event);
        JSONArray json_array_tran = (JSONArray)obj;
        json_array_tran.add(new Integer(index_last_tran));
        String jsonText_tran = json_array_tran.toString();
      
        // add เข้า array list
        Paying tran_add = new Paying(index_last_tran,money,name,"pay");
        tran_add.set(m_id);
        tran.add(tran_add);
        mem_bal = tran_add.update(mem_id,mem_bal);
        
        // ตอนเปลี่ยนเป็น jason creditor (user_id) (json)	accept (user_id) (json)
        JSONArray list_creditor_act = new JSONArray();
        JSONArray list_deftor_act = new JSONArray();
        for(int i = 0; i < m_id.length ;i++){
            list_creditor_act.add(new Integer(m_id[i]));
        }
        for(int i = 0; i < m_id.length ;i++){
            list_deftor_act.add(new Integer(m_id[i]));
        }
        String jsonText_creditor = list_creditor_act.toString();
        String jsonText_deftor = list_deftor_act.toString();
        //JSONArray list_mem_id = new JSONArray();
        // เข้า database
        
        
        /*
        database command
        */
        
        //  คำนวณ update
        
        //ดึง index_last_tran เข้า jason
    }
    ///////////////////////////////////////////////////////////////////// update command //////////////////////////////////////////////////////////////////
    public void update_bal_all(){
        //load information form database
        // algo
        int si = tran.size();
        for(int i =0 ; i< si ; i++){
            mem_bal = tran.get(i).update(mem_id,mem_bal);
        }
    }
    ///////////////////////////////////////////////////////////////////// private command //////////////////////////////////////////////////////////////////
    // get tran all
    public int[][] sort_bal(){
        int[][] pos_id = new int[mem_id.length][2];
        int[][] neg_id = new int[mem_id.length][2];
        int num_pos = 0;
        int num_neg = 0;
        int size = 0;
        
        for(int i = 0; i < mem_id.length; i++){
            if(mem_bal[i] >= 0){
                pos_id[num_pos][0] = i;
                pos_id[num_pos][1] = i;
                num_pos++;
            }else{
                neg_id[num_neg][0] = i;
                neg_id[num_neg][1] = i;
                num_neg++;
            }
        }
        if(num_pos >= num_neg){
            size = num_pos;
        }else{
            size = num_neg;
        } 
        int[][] bal_index = new int[size][2];
        
        return bal_index;
    }
    class Transaction {
        public int id;
        public String name;
        public String type = null;
        public double money;
        public Transaction (int id,double mo_ney,String name,String type){
            this.id = id;
            this.money = mo_ney;
            this.name = name;
            this.type = type;
        }
        public double[] update (int[] mem_id,double[] mem_bal){
            return mem_bal;
        }
}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////// sub class of transaction class  //////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////// Pay class  //////////////////////////////////////////////////////////////////
    class Paying extends Transaction {
        int payer_id[];
        public Paying (int id,double mo_ney,String name,String type){
            super(id, mo_ney,name,type);
        }
        public void set (int[] p_id){
            payer_id = p_id;
        }
        @Override
        public double[] update (int[] mem_id,double[] mem_bal){
            int size = mem_id.length;
            double[] new_mem_bal = new double[size];
            new_mem_bal = mem_bal;
            int i = 0;
            while(i < size){
                if(mem_id[i] == payer_id){
                    new_mem_bal[i] = new_mem_bal[i] - money;
                    break;
                }
                i++;
            }
            return new_mem_bal;
        }
    }

///////////////////////////////////////////////////// Accept class  //////////////////////////////////////////////////////////////////
    class Getting extends Transaction {
        int accepter_id[];
        public Getting (int id,double mo_ney,String name,String type){
            super(id, mo_ney,name,type);
        }
        public void set (int[] p_id){
            accepter_id = p_id;
        }
        @Override
        public double[] update (int[] mem_id,double[] mem_bal){
            int size = mem_id.length;
            double[] new_mem_bal = new double[size];
            new_mem_bal = mem_bal;
            int i = 0;
            while(i < size){
                if(mem_id[i] == accepter_id){
                    new_mem_bal[i] = new_mem_bal[i] + money;
                    break;
                }   
                i++;
            }
            return new_mem_bal;
        }
    }
///////////////////////////////////////////////////// Tranfer class  //////////////////////////////////////////////////////////////////
    class Tranfering extends Transaction {
        int[] accepter_id;
        int[] payer_id;
        public void set (int[] a_id,int[] p_id){
            accepter_id = a_id;
            payer_id = p_id;
        }
        public Tranfering (int id,double mo_ney,String name,String type){
            super(id, mo_ney,name,type);
        }
        @Override
        public double[] update (int[] mem_id,double[] mem_bal){
            int size = mem_id.length;
            double[] new_mem_bal = new double[size];
            new_mem_bal = mem_bal;
            int i = 0;
            int[] index = new int[2];
            boolean check1 = false;
            boolean check2 = false;
            while(i < size){
                if((mem_id[i] == accepter_id)&&(!check1)){
                    index[0] = i;
                }
                if((mem_id[i] == payer_id)&&(!check2)){
                    index[1] = i;
                }
                if(check1 && check2){
                    new_mem_bal[index[0]] = new_mem_bal[index[0]] + money;
                    new_mem_bal[index[1]] = new_mem_bal[index[1]] - money;
                    break;
                }
                i++;
            }
            return new_mem_bal;
        }
    }
    
    
}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////// sub class of transaction class  //////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class Group_event extends Event{
    
}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////// ฟังก์ช //////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

// public class member {

//}