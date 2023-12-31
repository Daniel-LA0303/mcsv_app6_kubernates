package org.springcloud.mcsv.users.mcsvusers.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "mcsv-cours", url = "${mcsv.cours.url}")
                                    //here is the uri or name of container
public interface CourseClirentRest {

    @DeleteMapping("/delete-user-by-id/{userId}")
    void deleteUserCourseById(@PathVariable Long userId);
}
