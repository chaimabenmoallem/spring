

package com.epix.hawkadmin.repository;

import com.epix.hawkadmin.model.Cars;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface CarsRepo extends ElasticsearchRepository <Cars, String> {

    List<Cars> findByEnergy(String energy);
    List<Cars> findByMake(String make);
    List<Cars> findByModel(String model);
    List<Cars> findByTransmissionType(String transmissionType);
    List<Cars> findByBodyType(String bodyType);
    Cars findByMakeAndModelAndEnergyAndTransmissionType(String make, String model, String energy, String transmissionType);
    List<Cars> findByMakeOrModelOrEnergyOrTransmissionType(String make, String model, String energy, String transmissionType);


    List<Cars> findByMakeAndModel(String make, String model);



   /* @Query(
            "{" +
                    "  \"bool\": {" +
                    "    \"must\": [" +
                    "      {\"match\": {\"make\": \"?0\"}}," +
                    "      {\"match\": {\"model\": \"?1\"}}," +
                    "      {\"match\": {\"energy\": \"?2\"}}," +
                    "      {\"match\": {\"transmission_type\": \"?3\"}}" +
                    "    ]" +
                    "  }" +
                    "}"
    )
    List<Cars> findByFlexibleAttributes(String make, String model, String energy, String transmissionType);*/

}












/*package com.epix.hawkadmin.repository;

import com.epix.hawkadmin.model.Cars;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface CarsRepo extends ElasticsearchRepository <Cars, String> {

    List<Cars> findByEnergy(String energy);
    List<Cars> findByMake(String make);
    List<Cars> findByModel(String model);
    List<Cars> findByTransmissionType(String transmissionType);
    List<Cars> findByBodyType(String bodyType);
    Cars findByMakeAndModelAndEnergyAndTransmissionType(String make, String model, String energy, String transmissionType);

    List<Cars> findByMakeAndModel(String make, String model);

    @Query("{\"bool\": {"
            + "\"must\": ["
            + "    {\"match\": {\"make\": \"?0\"}},"
            + "    {\"match\": {\"model\": \"?1\"}}"
            + "],"
            + "\"should\": ["
            + "    {\"match\": {\"energy\": \"?2\"}},"
            + "    {\"match\": {\"transmission_type\": \"?3\"}}"
            + "]"
            + "}}")
    List<Cars> findByFlexibleAttributes(String make, String model, String energy, String transmissionType);
    
}
*/
