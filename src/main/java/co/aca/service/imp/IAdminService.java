package co.aca.service.imp;

import co.aca.model.Admin;
import co.aca.model.User;

import java.util.List;

public interface IAdminService {

    List<Admin> getAdminList();

    Admin getAdminById(int id);

    Admin getAdminByUsername(String username);

    boolean addAdmin(Admin admin);

    void updateAdmin(Admin admin);

    void updateAdminFcm(Admin admin);

    boolean registerAdmin(List<Admin> adminListmin);

    boolean unregisterAdmin(List<Admin> adminListin);

    List<Admin> getRegisteredAdminList();

    List<Admin> getUnregisteredAdminList();

    void deleteAdmin(int id);

    void setLastLogin(Admin admin);
}