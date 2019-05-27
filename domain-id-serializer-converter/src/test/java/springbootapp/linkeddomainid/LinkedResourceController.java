package springbootapp.linkeddomainid;

import org.springframework.web.bind.annotation.*;


@RestController
public class LinkedResourceController {

    @GetMapping("/linked-get/{idOne}/stuff/{idTwo}")
    public LinkedResourceId linkedGet(@PathVariable("idOne") LinkedResourceId idOne, @PathVariable("idTwo") LinkedResourceId idTwo) {
        return new LinkedResourceId(idOne.toLong() + idTwo.toLong());
    }


    @PostMapping("/linked-post")
    public LinkedResource linkedPost(@RequestBody LinkedResource er) {
        return er;
    }

}
