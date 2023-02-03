package com.sci.cafe.serviceImpl;

import com.sci.cafe.dao.CategoriaDao;
import com.sci.cafe.dao.ContaDao;
import com.sci.cafe.dao.ProdutoDao;
import com.sci.cafe.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {
    @Autowired
    CategoriaDao categoriaDao;

    @Autowired
    ProdutoDao produtoDao;

    @Autowired
    ContaDao contaDao;

    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
        Map<String, Object> map = new HashMap<>();
        map.put("categoria", categoriaDao.count());
        map.put("produto", produtoDao.count());
        map.put("conta", contaDao.count());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
