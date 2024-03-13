package com.xust.sims.service;

import com.xust.sims.dao.MenuMapper;
import com.xust.sims.entity.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MenuServiceImpl implements MenuService{

    @Autowired
    private MenuMapper menuMapper;

    @Override
    @Cacheable(value = "menus_list", key = "'menus_' + #status")
    public List<Menu> initMenus(Integer status) {
        return menuMapper.findAllMenusByStatus(status);
    }

}
