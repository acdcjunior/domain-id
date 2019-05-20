package springbootapp;

import org.springframework.web.bind.annotation.*;


@RestController
public class ExampleController {

    @GetMapping("/example-get/{idOne}/stuff/{idTwo}")
    public ExampleResourceId stuff(@PathVariable("idOne") ExampleResourceId idOne, @PathVariable("idTwo") ExampleResourceId idTwo) {
        return new ExampleResourceId(idOne.toLong() + idTwo.toLong());
    }


    @PostMapping("/example-post")
    public ExampleResource examplePost(@RequestBody ExampleResource er) {
        return er;
    }

}
