package com.sci.cafe.serviceImpl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sci.cafe.JWT.JwtFilter;
import com.sci.cafe.model.Conta;
import com.sci.cafe.constantes.CafeConstantes;
import com.sci.cafe.dao.ContaDao;
import com.sci.cafe.service.ContaService;
import com.sci.cafe.utils.CafeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.IOUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
public class ContaServiceImpl implements ContaService {

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    ContaDao contaDao;

    @Override
    public ResponseEntity<String> gerarReport(Map<String, Object> requestMap) {
        log.info("Dentro do gerarReport");

        try {
            String nomeArquivo;
            if(validarRequestMap(requestMap)){
                if(requestMap.containsKey("isGenerate") && !(Boolean) requestMap.get("isGenerate")){
                    nomeArquivo = (String) requestMap.get("uuid");
                }else{
                    nomeArquivo = CafeUtils.getUUID();
                    requestMap.put("uuid", nomeArquivo);
                    cadastrarConta(requestMap);
                }

                String data = "Nome: "+requestMap.get("nome") +"\n"+"Numero Contato: "+requestMap.get("numeroContato")+
                                "\n"+"Email: "+requestMap.get("email")+"\n"+"Metodo Pagamento: "+requestMap.get("metodoPagamento");

                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(CafeConstantes.STORE_LOCATION + "\\" + nomeArquivo + ".pdf"));

                document.open();
                setRetanguloPdf(document);

                Paragraph chunk = new Paragraph("Sistema de Gerenciamento Cafeteria", getFont("Header"));
                chunk.setAlignment(Element.ALIGN_CENTER);
                document.add(chunk);

                Paragraph paragraph = new Paragraph(data+ "\n \n", getFont("Dados"));
                document.add(paragraph);

                PdfPTable table = new PdfPTable(5);
                table.setWidthPercentage(100);
                addTableHeader(table);

                JSONArray jsonArray = CafeUtils.getJsonArrayFromString((String) requestMap.get("produtoDetalhes"));

                for(int i = 0; i < jsonArray.length(); i++){
                    addRows(table, CafeUtils.getMapFromJson(jsonArray.getString(i)));
                }
                document.add(table);

                Paragraph footer = new Paragraph("Total: "+requestMap.get("valorTotal")+"\n"
                        + "Obrigado por nos visitar. Volte novamente!!", getFont("Data"));
                document.add(footer);
                document.close();
                return new ResponseEntity<>("{\"uuid\":\"" + nomeArquivo + "\"}", HttpStatus.OK);
            }
            return CafeUtils.getResponseEntity("Dados necessarios não encontrados", HttpStatus.BAD_REQUEST);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }



    private void addRows(PdfPTable table, Map<String, Object> data) {
        log.info("Dentro addRows");
        table.addCell((String) data.get("nome"));
        table.addCell((String) data.get("categoria"));
        table.addCell((String) data.get("quantidade"));
        table.addCell(Double.toString((Double) data.get("preco")));
        table.addCell(Double.toString((Double) data.get("total")));
    }

    private void addTableHeader(PdfPTable table) {
        log.info("Dentro addTableHeader");
        Stream.of("Nome", "Categoria", "Quantidade", "Preco", "Sub Total")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    header.setBackgroundColor(BaseColor.YELLOW);
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setVerticalAlignment(Element.ALIGN_CENTER);
                    table.addCell(header);

                });
    }

    private Font getFont(String type) {
        log.info("Dentro do getFont");
        switch (type){
            case "Header":
                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 18, BaseColor.BLACK);
                headerFont.setStyle(Font.BOLD);
                return headerFont;

            case "Data":
                Font dataFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, BaseColor.BLACK);
                dataFont.setStyle(Font.BOLD);
                return dataFont;

            default:
                return new Font();

        }

    }

    private void setRetanguloPdf(Document document) throws DocumentException {
        log.info("Dentro do setRetanguloPdf");
        Rectangle rect = new Rectangle(577, 825, 18, 15);
        rect.enableBorderSide(1);
        rect.enableBorderSide(2);
        rect.enableBorderSide(4);
        rect.enableBorderSide(8);
        rect.setBorderColor(BaseColor.BLACK);
        rect.setBorderWidth(1);
        document.add(rect);
    }

    private void cadastrarConta(Map<String, Object> requestMap) {
        try {
            Conta conta = new Conta();
            conta.setUuid((String) requestMap.get("uuid"));
            conta.setNome((String) requestMap.get("nome"));
            conta.setEmail((String) requestMap.get("email"));
            conta.setNumeroContato((String) requestMap.get("numeroContato"));
            conta.setMetodoPagamento((String) requestMap.get("metodoPagamento"));
            conta.setTotal(Integer.parseInt((String) requestMap.get("valorTotal")));
            conta.setProdutoDetalhes((String) requestMap.get("produtoDetalhes"));
            conta.setCriadoPor(jwtFilter.getCurrentUser());
            contaDao.save(conta);

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private boolean validarRequestMap(Map<String, Object> requestMap) {
        return requestMap.containsKey("nome") &&
                requestMap.containsKey("numeroContato") &&
                requestMap.containsKey("email") &&
                requestMap.containsKey("metodoPagamento") &&
                requestMap.containsKey("produtoDetalhes") &&
                requestMap.containsKey("valorTotal");
    }

    @Override
    public ResponseEntity<List<Conta>> getContas() {
        List<Conta>  list = new ArrayList<>();
        if(jwtFilter.isAdmin()){
            list = contaDao.getTodasContas();
        }else {
            list = contaDao.getContaPeloUsername(jwtFilter.getCurrentUser());
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {
        log.info("Dentro do getPdf: requestMap {}", requestMap);
        try {
            byte[] byteArray = new byte[0];
            if(!requestMap.containsKey("uuid") && validarRequestMap(requestMap))
                return new ResponseEntity<>(byteArray, HttpStatus.BAD_REQUEST);
            String filePath = CafeConstantes.STORE_LOCATION+"\\"+(String) requestMap.get("uuid") + ".pdf";
            if(CafeUtils.arquivoJaExiste(filePath)){
                byteArray = getByteArray(filePath);
                return new ResponseEntity<>(byteArray, HttpStatus.OK);
            }
            else{
                requestMap.put("isGenerate", false);
                gerarReport(requestMap);
                byteArray = getByteArray(filePath);
                return new ResponseEntity<>(byteArray, HttpStatus.OK);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    private byte[] getByteArray(String filePath) throws Exception{
        File initialFile = new File(filePath);
        InputStream targetStream = new FileInputStream(initialFile);
        byte[] byteArray = IOUtils.toByteArray(targetStream);
        targetStream.close();
        return byteArray;
    }

    @Override
    public ResponseEntity<String> excluirConta(Integer id) {
        try {
            Optional optional = contaDao.findById(id);
            if(!optional.isEmpty()){
                contaDao.deleteById(id);
                return CafeUtils.getResponseEntity("Conta excluida com Sucesso", HttpStatus.OK);
            }
            return CafeUtils.getResponseEntity("Conta não existe", HttpStatus.OK);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
