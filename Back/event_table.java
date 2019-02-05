/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Lenovo
 */
class event_list {
    public String name;
    public int id;
    public String type; 
    public boolean group;
    public int num_member; // M =1, F=0ng tel;
    public int start_date;
    public int due_date;
    ////////////////////////////////////////////////////////////// set command /////////////////////////////////////////////////////
    public event_list(String name,int id, String type,boolean group, int num_member,int start_date,int due_date){
        this.name = name;
        this.id = id;
        this.type = type; 
        this.group = group;
        this.num_member = num_member; 
        this.start_date = start_date;
        this.due_date = due_date;
    }
}
