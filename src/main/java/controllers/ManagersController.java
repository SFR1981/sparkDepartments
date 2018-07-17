package controllers;

import db.DBHelper;
import models.Department;
import models.Employee;
import models.Manager;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.post;

public class ManagersController {

    public ManagersController(){
        this.setupEndPoints();

    }

    private void setupEndPoints(){
        //TODO: Add routes in here
        get("/managers", (req,res) ->{
            Map<String, Object> model = new HashMap();
            model.put("template", "templates/managers/index.vtl");

            List<Manager> managers = DBHelper.getAll(Manager.class);
            model.put("managers", managers);

            return new ModelAndView(model, "templates/layout.vtl");

        }, new VelocityTemplateEngine());

        get("/managers/new", (req,res) ->{
            Map<String, Object> model = new HashMap();
            List<Department> departments = DBHelper.getAll(Department.class);
            model.put("departments", departments);
            model.put("template", "templates/managers/create.vtl");
            return new ModelAndView(model, "templates/layout.vtl");

        }, new VelocityTemplateEngine());

        post("/managers", (req,res) ->{
            // get firstName from request
            String firstName = req.queryParams("firstName");
            //get lastName from request
            String lastName = req.queryParams("lastName");
            // get salary from request
            int salary = Integer.parseInt(req.queryParams("salary"));
            // get budget from request
            Double budget = Double.parseDouble(req.queryParams("budget"));
            // get department id
            System.out.println(req.queryParams("department"));
            int departmentId = Integer.parseInt(req.queryParams("department"));
            // get department from id
            Department department = DBHelper.find(departmentId, Department.class);

            Manager newManager = new Manager(firstName, lastName, salary, department, budget);
            DBHelper.save(newManager);
            res.redirect("/managers");
            return null;

        }, new VelocityTemplateEngine());


        get("/managers/:id/edit", (req, res) -> {
            Map<String, Object> model = new HashMap();
            int id = Integer.parseInt(req.params(":id"));
            Manager manager = DBHelper.find(id, Manager.class);
            model.put("manager",manager);
            List<Department> departments = DBHelper.getAll(Department.class);
            model.put("departments", departments);
            model.put("template", "templates/managers/edit.vtl");
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());


        post("/managers/:id", (req,res) ->{

            int managerId = Integer.parseInt(req.params(":id"));
            // get firstName from request
            String firstName = req.queryParams("firstName");
            //get lastName from request
            String lastName = req.queryParams("lastName");
            // get salary from request
            int salary = Integer.parseInt(req.queryParams("salary"));
            // get budget from request
            Double budget = Double.parseDouble(req.queryParams("budget"));
            // get department id
            int departmentId = Integer.parseInt(req.queryParams("department"));
            // get department from id
            Department department = DBHelper.find(departmentId, Department.class);

            Manager manager = DBHelper.find(managerId, Manager.class);
            manager.setFirstName(firstName);
            manager.setLastName(lastName);
            manager.setSalary(salary);
            manager.setBudget(budget);
            manager.setDepartment(department);
            DBHelper.save(manager);
            res.redirect("/managers");
            return null;

        }, new VelocityTemplateEngine());

        get ("/managers/:id", (req, res)->{
            Map<String, Object> model = new HashMap<>();
            int id = Integer.parseInt(req.params(":id"));
            Manager manager = DBHelper.find(id, Manager.class);
            model.put("manager", manager);
            model.put("template", "templates/managers/show.vtl");
            return new ModelAndView(model, "templates/layout.vtl");

        }, new VelocityTemplateEngine());

        post ("/managers/:id/delete", (req, res) -> {

            int managerId = Integer.parseInt(req.params(":id"));
            Manager manager = DBHelper.find(managerId, Manager.class);
            DBHelper.delete(manager);

            res.redirect("/managers");
            return null;
        }, new VelocityTemplateEngine());




    }


}
