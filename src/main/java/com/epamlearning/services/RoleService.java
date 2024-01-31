package com.epamlearning.services;

import com.epamlearning.entities.Role;
import com.epamlearning.entities.enums.RoleName;

public interface RoleService extends BaseService{

    Role findByRoleName(RoleName name);

}
