package com.example.ecomerce.service.Impl;
import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.PagedResponse;
import com.example.ecomerce.dto.VerificationTokenResponseDto;
import com.example.ecomerce.entity.Verification;
import com.example.ecomerce.enums.Error;
import com.example.ecomerce.exception.ErrorVerificationToken;
import com.example.ecomerce.repository.VerificationTokenRepository;
import com.example.ecomerce.service.VerificationTokenService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
public class VerificationTokenImpl implements VerificationTokenService {
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ResponseEntity<ApiResponse> getAllTokens(Pageable pageable){
        Page<Verification> verifications = verificationTokenRepository.findAll(pageable);
        List<VerificationTokenResponseDto> verificationTokenResponseDtos = verifications.getContent().stream()
                .map(verification -> modelMapper.map(verification, VerificationTokenResponseDto.class))
                .toList();
        PagedResponse<VerificationTokenResponseDto> pagedResponse = new PagedResponse<>(verificationTokenResponseDtos, verifications);
        return ResponseEntity.ok(new ApiResponse("List verification ", pagedResponse, "SUCCESS"));
    }


    @Override
    public  ResponseEntity<ApiResponse> deleteToken(int id){
        Verification verification = verificationTokenRepository.findById(id).orElse(null);
        if (verification == null){
            return ResponseEntity.badRequest().body(new ApiResponse("token does not exist", null, ErrorVerificationToken.INVALID_TOKEN));
        }
        verificationTokenRepository.deleteById(id);
        return ResponseEntity.ok(new ApiResponse("delete token success",null,"SUCCESS"));
    }



}
