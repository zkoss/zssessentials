package org.zkoss.zss.essential.advanced.permission;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;

public class LoginComposer extends SelectorComposer<Component> {

	static List<Role.Name> roles = AuthorityService.getRoles();

	@Wire("combobox")
	Combobox roleBox;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		ListModelList<Role.Name> model = new ListModelList<Role.Name>(roles);
		model.addToSelection(model.get(0));
		roleBox.setModel(model);
	}
	
	@Listen("onClick = button")
	public void login(){
		Executions.getCurrent().getSession().setAttribute("role", roles.get(roleBox.getSelectedIndex()));
		Executions.getCurrent().sendRedirect("main.zul");
	}
}
