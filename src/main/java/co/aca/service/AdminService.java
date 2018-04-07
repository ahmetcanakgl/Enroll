package co.aca.service;

import co.aca.dao.imp.IAdminDAO;
import co.aca.model.Admin;
import co.aca.service.imp.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService implements IAdminService {

    @Autowired
    private IAdminDAO adminDAO;

    @Override
    public Admin getAdminById(int id) {
        Admin obj = adminDAO.getAdminById(id);
        return obj;
    }

    @Override
    public Admin getAdminByUsername(String username) {
        Admin obj = adminDAO.getAdminByUsername(username);
        return obj;
    }

    @Override
    public List<Admin> getAdminList() {
        return adminDAO.getAdminList();
    }

    @Override
    public synchronized boolean addAdmin(Admin admin) {
        if (adminDAO.adminExists(admin.getAdminUsername())) {
            return false;
        } else {
            adminDAO.addAdmin(admin);
            return true;
        }
    }

    @Override
    public void updateAdmin(Admin admin) {
        adminDAO.updateAdmin(admin);
    }

    @Override
    public void updateAdminFcm(Admin admin) {
        adminDAO.updateAdminFcm(admin);
    }

    @Override
    public void deleteAdmin(int id) {
        adminDAO.deleteAdmin(id);
    }

    @Override
    public void setLastLogin(Admin admin) {
        adminDAO.setLastLogin(admin);
    }

    @Override
    public synchronized boolean registerAdmin(List<Admin> adminList) {

        Boolean flag = true;

        for (int i = 0; i < adminList.size(); i++) {
            if (adminDAO.isAdminRegistered(adminList.get(i).getAdminId())) {
                flag = false;
            } else {
                adminDAO.registerAdmin(adminList.get(i));
            }
        }

        return flag;
    }

    @Override
    public synchronized boolean unregisterAdmin(List<Admin> adminList) {

        Boolean flag = true;

        for (int i = 0; i < adminList.size(); i++) {

            if (adminDAO.isAdminRegistered(adminList.get(i).getAdminId())) {
                adminDAO.unregisterAdmin(adminList.get(i));
            } else {
                flag = false;
            }
        }

        return flag;
    }

    public List<Admin> getRegisteredAdminList() {
        return adminDAO.getRegisteredAdminList();
    }

    public List<Admin> getUnregisteredAdminList() {
        return adminDAO.getUnregisteredAdminList();
    }
}
