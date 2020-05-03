package com.weiss.weiss.services;

import com.weiss.weiss.dao.NominationTimeDao;
import com.weiss.weiss.model.NominationTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicInteger;

@EnableAsync
@Service
public class NominationService {
    @Autowired
    NominationTimeDao nominationTimeDao;
    AtomicInteger nominationTime;
    AtomicInteger timeoutTime;
    @Value("${nomination.defaultTime}")
    int defaultTime;
    @Value("${nomination.defaultTimeout}")
    int defaultTimeout;
    @Value("${nomination.updateRate}")
    int updateRate;

    @PostConstruct
    private void init(){
        NominationTime nt = nominationTimeDao.getNominationTime();
        if(nt!=null){
        setNominationTime(new AtomicInteger(nt.getTime()));
        setNominationTimeOut(new AtomicInteger(nt.getTimeout()));}else {
            setNominationTime(new AtomicInteger(defaultTime));
            setNominationTimeOut(new AtomicInteger(defaultTimeout));
        }
    }
    @Async
    @Scheduled(fixedRateString  = "${nomination.updateRate}")
    void decreaseTimer(){
        int i = this.nominationTime.updateAndGet(x ->x<updateRate?0:x - updateRate);
        if(i<=0){
            if(this.timeoutTime.get()>0){
                this.timeoutTime.updateAndGet(x ->x<updateRate?0:x - updateRate);
                //emit event to calculate Champion
            }else {
                setNominationTime(new AtomicInteger(defaultTime));
                setNominationTimeOut(new AtomicInteger(defaultTimeout));
            }
        }
    }
    @Async
    @Scheduled(fixedRateString = "${nomination.saveRate}")
    void saveTimers(){
        nominationTimeDao.saveTimers(NominationTime.builder().time(getNominationTime()).timeout(getNominationTimeOut()).build());
    }


    public synchronized void setNominationTime(AtomicInteger time) {
        this.nominationTime = time;
    }
    public synchronized void setNominationTimeOut(AtomicInteger time) {
        this.timeoutTime = time;
    }

    public int getNominationTime() {
        return nominationTime.get();
    }
    public int getNominationTimeOut() {
        return timeoutTime.get();
    }
}
