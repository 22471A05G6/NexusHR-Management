package com.hr_management.Security;


import java.util.Arrays;
import java.util.*;
import java.util.Map;
import java.util.Set;

import com.hr_management.ENUM.Permission;
import com.hr_management.ENUM.Role;

public class RoleBasedPermissions {
	public static Map<Role,Set<Permission>>getRoleBasedPermissions(){
		Map<Role,Set<Permission>>perm=new HashMap<>();
		
		perm.put(Role.ADMIN,new HashSet<Permission>(Arrays.asList(
				                                                Permission.CREATE_EMPLOYEE,
				                                                Permission.UPDATE_EMPLOYEE,
				                                                Permission.DELETE_EMPLOYEE,
				                                                Permission.VIEW_PAYROLL,
				                                                Permission.RUN_PAYROLL,
				                                                Permission.APPROVE_LEAVE,
				                                                Permission.APPROVE_WFH,
				                                                Permission.VIEW_ANALYTICS,
				                                                Permission.VIEW_HOLIDAY,
				                                                Permission.VIEW_EMPLOYEE)));
		perm.put(Role.HR, new HashSet<Permission>(Arrays.asList(
				                                               Permission.CREATE_EMPLOYEE,
				                                               Permission.UPDATE_EMPLOYEE,
				                                               Permission.VIEW_EMPLOYEE,
				                                               Permission.APPROVE_LEAVE,
				                                               Permission.APPLY_LEAVE,
				                                               Permission.APPLY_WFH,
				                                               Permission.APPROVE_WFH,
				                                               Permission.VIEW_PAYROLL,
				                                               Permission.VIEW_HOLIDAY)));
		perm.put(Role.EMPLOYEE, new HashSet<Permission>(Arrays.asList(
				                                                     Permission.APPLY_LEAVE,
				                                                     Permission.VIEW_ATTENDANCE,
				                                                     Permission.VIEW_HOLIDAY,
				                                                     Permission.VIEW_PAYROLL,
				                                                     Permission.APPLY_WFH)));
		return perm;
				                                               
				
				
		
	}

}
