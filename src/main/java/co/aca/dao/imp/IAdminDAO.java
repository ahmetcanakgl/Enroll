package co.aca.dao.imp;

import co.aca.model.Admin;

import java.util.List;

public interface IAdminDAO {

    List<Admin> getAdminList();

    Admin getAdminById(int id);

    Admin getAdminByUsername(String username);

    void addAdmin(Admin admin);

    void updateAdmin(Admin admin);

    void updateAdminFcm(Admin admin);

    void deleteAdmin(int id);

    boolean adminExists(String username);

    boolean isAdminRegistered(int adminId);

    void registerAdmin(Admin admin);

    void unregisterAdmin(Admin admin);

    List<Admin> getRegisteredAdminList();

    List<Admin> getUnregisteredAdminList();

    void setLastLogin(Admin admin);
}
