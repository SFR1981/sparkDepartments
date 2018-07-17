package controllers;

import db.DBHelper;
import models.Department;
import models.Engineer;
import models.Manager;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.post;

public class EngineersController {

    public EngineersController(){
        this.setupEndPoints();

    }

    public void setupEndPoints(){

        //TODO: add routes here
        get("/engineers", (req,res) ->{
            Map<String, Object> model = new HashMap();
            model.put("template", "templates/engineers/index.vtl");

            List<Engineer> engineers = DBHelper.getAll(Engineer.class);
            model.put("engineers", engineers);

            return new ModelAndView(model, "templates/layout.vtl");
        },new VelocityTemplateEngine());

        get("/engineers/new", (req,res) ->{
            Map<String, Object> model = new HashMap();
            List<Department> departments = DBHelper.getAll(Department.class);
            model.put("departments", departments);
            model.put("template", "templates/engineers/create.vtl");
            return new ModelAndView(model, "templates/layout.vtl");

        }, new VelocityTemplateEngine());

        post("/engineers", (req,res) ->{
            // get firstName from request
            String firstName = req.queryParams("firstName");
            //get lastName from request
            String lastName = req.queryParams("lastName");
            // get salary from request
            int salary = Integer.parseInt(req.queryParams("salary"));
            // get department id
            int departmentId = Integer.parseInt(req.queryParams("department"));
            // get department from id
            Department department = DBHelper.find(departmentId, Department.class);

            Engineer engineer = new Engineer(firstName, lastName, salary, department);
            DBHelper.save(engineer);
            res.redirect("/engineers");
            return null;

        }, new VelocityTemplateEngine());
    }
}
