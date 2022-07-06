package kz.halykacademy.app.service;

import com.google.gson.Gson;
import kz.halykacademy.app.model.Organization;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class OrganizationService {
    private Map<Long, Organization> mapOrganizations;
    private AtomicLong key;

    public OrganizationService() {
        this.mapOrganizations = new ConcurrentHashMap<>();
        key = new AtomicLong();

        this.addOrganization(new Organization("Ak Orda", "West", LocalDate.now()));
        this.addOrganization(new Organization("Kyzyl Orda", "South", LocalDate.now()));
    }

    // GET
    public String findAll() {
        List<Organization> listOrganizations = new ArrayList<>(this.mapOrganizations.values());
        return convertToJson(listOrganizations);
    }

    // POST
    public String createOrganization(String jsonPayload) {
        if (jsonPayload == null) return null;

        Gson gson = new Gson();
        try {
            Organization organization = gson.fromJson(jsonPayload, Organization.class);
            if (organization != null) {
                return convertToJson(this.addOrganization(organization));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // DELETE
    public boolean deleteOrganization(Long id) {
        Organization removeOrganization = mapOrganizations.remove(id);
        if (removeOrganization != null) {
            return true;
        }
        return false;
    }

    private String convertToJson(Object value) {
        if (value == null) return null;
        Gson gson = new Gson();
        String jsonAsString = null;

        try {
            jsonAsString = gson.toJson(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonAsString;
    }

    private Organization addOrganization(Organization organization) {
        Long id = key.incrementAndGet();
        organization.setId(id);
        mapOrganizations.put(id, organization);
        return organization;
    }
}
