package com.example.resource_client_1.controller;


import com.example.resource_client_1.models.MasterResponse;
import com.example.resource_client_1.models.RecordRequestModel;
import com.example.resource_client_1.models.RecordResponseModel;
import com.example.resource_client_1.models.Status;
import com.example.resource_client_1.services.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/records")
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;


    // ---------------------------------------------
    // SUCCESS STATUS GENERATOR
    // ---------------------------------------------
    private Status successStatus() {
        return Status.builder()
                .statusCode(200)
                .message("SUCCESS")
                .helperMessage(null)
                .build();
    }


    // ---------------------------------------------
    // CREATE
    // ---------------------------------------------
    @PostMapping
    public ResponseEntity<MasterResponse<RecordResponseModel>> createRecord(
            @RequestBody RecordRequestModel request) {

        RecordResponseModel response = recordService.createRecord(request);

        return ResponseEntity.ok(
                MasterResponse.<RecordResponseModel>builder()
                        .status(successStatus())
                        .data(response)
                        .build()
        );
    }


    // ---------------------------------------------
    // GET ALL
    // ---------------------------------------------
    @GetMapping
    public ResponseEntity<MasterResponse<List<RecordResponseModel>>> getAllRecords() {

        List<RecordResponseModel> responseList = recordService.getAllRecords();

        return ResponseEntity.ok(
                MasterResponse.<List<RecordResponseModel>>builder()
                        .status(successStatus())
                        .data(responseList)
                        .build()
        );
    }


    // ---------------------------------------------
    // GET BY ID
    // ---------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<MasterResponse<RecordResponseModel>> getRecordById(
            @PathVariable String id) {

        RecordResponseModel response = recordService.getRecordById(id);

        return ResponseEntity.ok(
                MasterResponse.<RecordResponseModel>builder()
                        .status(successStatus())
                        .data(response)
                        .build()
        );
    }


    // ---------------------------------------------
    // UPDATE
    // ---------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<MasterResponse<RecordResponseModel>> updateRecord(
            @PathVariable String id,
            @RequestBody RecordRequestModel request) {

        RecordResponseModel updated = recordService.updateRecord(id, request);

        return ResponseEntity.ok(
                MasterResponse.<RecordResponseModel>builder()
                        .status(successStatus())
                        .data(updated)
                        .build()
        );
    }


    // ---------------------------------------------
    // DELETE
    // ---------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<MasterResponse<String>> deleteRecord(
            @PathVariable String id) {

        recordService.deleteRecord(id);

        return ResponseEntity.ok(
                MasterResponse.<String>builder()
                        .status(successStatus())
                        .data("Deleted successfully")
                        .build()
        );
    }
}
