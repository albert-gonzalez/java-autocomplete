package games.infrastructure.delivery.api.controllers;

import play.mvc.Controller;
import play.mvc.Result;


public class TestController extends Controller {

    public Result test() {
        return ok("ok");
    }

}
