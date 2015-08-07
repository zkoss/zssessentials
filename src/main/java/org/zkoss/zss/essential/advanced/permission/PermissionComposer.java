package org.zkoss.zss.essential.advanced.permission;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zul.A;

public class PermissionComposer extends SelectorComposer<Component> {
	@Wire
	private Spreadsheet ss;
	@Wire("a")
	private A logoutLink;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Role.Name role = (Role.Name)Executions.getCurrent().getSession().getAttribute("role");
		if (role == null){
			Executions.getCurrent().sendRedirect("login.zul"); //prevent unauthorized access
		}else{
			logoutLink.setLabel(logoutLink.getLabel()+" - "+role.name());
			AuthorityService.applyPermission(ss, role); //apply the role's permission
		}
	}
}
