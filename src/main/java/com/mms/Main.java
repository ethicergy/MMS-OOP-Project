package com.mms;

import com.mms.db.DBManager;

public class Main {
public static void main(String[] args){
	try(DBManager db = new DBManager()) {
	System.out.println("Shi working till now");
	} catch (Exception e) {
	e.printStackTrace();
	}
	}
	}
