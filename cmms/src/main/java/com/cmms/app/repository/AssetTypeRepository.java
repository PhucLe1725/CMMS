package com.cmms.app.repository;

import com.cmms.app.entity.AssetType;
import com.cmms.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AssetTypeRepository extends JpaRepository<AssetType, Long> {


    @Query("SELECT a FROM AssetType a")
    List<AssetType> findAllAssetType();
}
