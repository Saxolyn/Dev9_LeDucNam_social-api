package com.social.socialserviceapp.service.common;

import com.social.socialserviceapp.exception.SocialAppException;
import com.social.socialserviceapp.repository.common.BaseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public abstract class BaseService<E, F> {
    protected abstract BaseRepository<E, Long> getBaseRepository();

    protected void validate(F requestDTO) throws SocialAppException{
    }

    protected E customBeforeSave(F requestDTO){
        return null;
    }

    @Transactional
    public E saveOrUpdate(F form) throws Exception, SocialAppException{
        validate(form);
        E entity = customBeforeSave(form);
        getBaseRepository().save(entity);
        return entity;
    }
}
