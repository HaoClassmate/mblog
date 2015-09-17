package mblog.persist.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import mblog.data.AuthMenu;
import mblog.persist.dao.AuthMenuDao;
import mblog.persist.entity.AuthMenuPO;
import mblog.persist.service.AuthMenuService;
import mblog.persist.utils.BeanMapUtils;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public class AuthMenuServiceImpl implements AuthMenuService {
	
	@Autowired
	private AuthMenuDao authMenuDao;

	@Override
	public List<AuthMenu> findByParentId(long parentId) {
		// TODO Auto-generated method stub
		List<AuthMenu> authMenus = new ArrayList<AuthMenu>();
		List<AuthMenuPO> authMenuPOs = authMenuDao.findByParentId(parentId);
		if(authMenuPOs!=null){
			for(AuthMenuPO po : authMenuPOs){
				AuthMenu authMenu = BeanMapUtils.copy(po);
				authMenus.add(authMenu);
			}
		}
		return authMenus;
	}

	@Override
	public List<AuthMenu> tree(Long id) {
		List<AuthMenu> menus = new ArrayList<>();
		AuthMenuPO authMenuPO = authMenuDao.get(id);
		AuthMenu authMenu = BeanMapUtils.copy(authMenuPO);
		menus.add(authMenu);
		if(authMenu.getChildren()!=null){
			List<AuthMenu> sortedList = sort(authMenu.getChildren());
			for (AuthMenu po: authMenu.getChildren()){
				menus.addAll(tree(po.getId()));
			}
		}
		return menus;
	}

	@Override
	public AuthMenu get(Long id) {
		AuthMenu authMenu = BeanMapUtils.copy(authMenuDao.get(id));
		return authMenu;
	}

	private List<AuthMenu> sort(List<AuthMenu> list) {
		for(int i=0;i<list.size();i++){
			for(int j=list.size()-1;j>i;j--){
				if(list.get(i).getSort()>list.get(j).getSort()){
					AuthMenu temp = list.get(i);
					list.set(i,list.get(j));
					list.set(j,temp);
				}
			}
		}
		return list;
	}

}
