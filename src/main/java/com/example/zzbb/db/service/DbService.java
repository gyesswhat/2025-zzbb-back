package com.example.zzbb.db.service;

import com.example.zzbb.db.dto.BriefDbResponse;
import com.example.zzbb.db.dto.DbListResponse;
import com.example.zzbb.db.dto.DbResponse;
import com.example.zzbb.db.entity.Db;
import com.example.zzbb.db.entity.DbHashtag;
import com.example.zzbb.db.entity.DbImage;
import com.example.zzbb.db.repository.DbRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class DbService {
    private final DbRepository dbRepository;

    public ArrayList<BriefDbResponse> viewBriefDb() {
        // 1. Db 불러오기
        ArrayList<Db> dbs = dbRepository.getLatestDbs(PageRequest.of(0, 10));
        if (dbs == null) return null;

        // 2. response 형식으로 변환
        ArrayList<BriefDbResponse> responses = new ArrayList<>();
        for (Db db : dbs) {
            responses.add(new BriefDbResponse(
                    db.getDbId(),
                    db.getTitle()
            ));
        }

        // 3. 반환
        return responses;
    }

    public ArrayList<DbListResponse> viewDbList() {
        // 1. Db 불러오기
        ArrayList<Db> dbs = (ArrayList<Db>) dbRepository.findAll();
        if (dbs == null) return null;

        // 2. response 형식으로 변환
        ArrayList<DbListResponse> responses = new ArrayList<>();
        for (Db db: dbs) {
            ArrayList<String> dbHashtagResponse = new ArrayList<>();
            for (DbHashtag dbHashtag : db.getDbHashtags())
                dbHashtagResponse.add(dbHashtag.getTag());
            responses.add(new DbListResponse(
                    db.getDbId(),
                    db.getTitle(),
                    db.getBody(),
                    dbHashtagResponse
            ));
        }

        // 3. 반환
        return responses;
    }

    public DbResponse viewDb(Integer dbId) {
        // 1. Db 불러오기
        Db db = dbRepository.findById(dbId).orElse(null);
        if (db == null) return null;

        ArrayList<String> dbHashTagResponse = new ArrayList<>();
        ArrayList<String> dbImageResponse = new ArrayList<>();
        for (DbHashtag dbHashtag : db.getDbHashtags())
            dbHashTagResponse.add(dbHashtag.getTag());
        for (DbImage dbImage : db.getImages())
            dbImageResponse.add(dbImage.getUrl());

        // 2. response 형식으로 변환
        DbResponse response = new DbResponse(
                db.getDbId(),
                db.getTitle(),
                db.getBody(),
                dbHashTagResponse,
                dbImageResponse
        );

        // 3. 반환
        return response;
    }
}
