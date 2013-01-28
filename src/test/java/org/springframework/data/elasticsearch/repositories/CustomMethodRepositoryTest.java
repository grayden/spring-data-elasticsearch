package org.springframework.data.elasticsearch.repositories;


import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.SampleEntity;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.DeleteQuery;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang.RandomStringUtils.randomNumeric;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:custom-method-repository-test.xml")
public class CustomMethodRepositoryTest {

    @Resource
    private SampleCustomMethodRepository repository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Before
    public void before(){
        elasticsearchTemplate.createIndex(SampleEntity.class);
        DeleteQuery deleteQuery = new DeleteQuery();
        deleteQuery.setElasticsearchQuery(matchAllQuery());
        elasticsearchTemplate.delete(deleteQuery,SampleEntity.class);
        elasticsearchTemplate.refresh(SampleEntity.class, true);
    }

    @Test
    public void shouldExecuteCustomMethod(){
        //given
        String documentId = randomNumeric(5);
        SampleEntity sampleEntity = new SampleEntity();
        sampleEntity.setId(documentId);
        sampleEntity.setType("test");
        sampleEntity.setMessage("some message");
        repository.save(sampleEntity);
        //when
        Page<SampleEntity> page = repository.findByType("test", new PageRequest(1, 10));
        //then
        assertThat(page, is(notNullValue()));
        assertThat(page.getTotalElements(), is(greaterThanOrEqualTo(1L)));
    }

    @Test
    public void shouldExecuteCustomMethodForNot(){
        //given
        String documentId = randomNumeric(5);
        SampleEntity sampleEntity = new SampleEntity();
        sampleEntity.setId(documentId);
        sampleEntity.setType("some");
        sampleEntity.setMessage("some message");
        repository.save(sampleEntity);
        //when
        Page<SampleEntity> page = repository.findByTypeNot("test", new PageRequest(1, 10));
        //then
        assertThat(page, is(notNullValue()));
        assertThat(page.getTotalElements(), is(equalTo(1L)));
    }

    @Test
    public void shouldExecuteCustomMethodWithQuery(){
        //given
        String documentId = randomNumeric(5);
        SampleEntity sampleEntity = new SampleEntity();
        sampleEntity.setId(documentId);
        sampleEntity.setType("test");
        sampleEntity.setMessage("customQuery");
        repository.save(sampleEntity);
        //when
        Page<SampleEntity> page  = repository.findByMessage("customQuery", new PageRequest(1, 10));
        //then
        assertThat(page, is(notNullValue()));
        assertThat(page.getTotalElements(), is(greaterThanOrEqualTo(1L)));
    }


    @Test
    public void shouldExecuteCustomMethodWithLessThan(){
        //given
        String documentId = randomNumeric(5);
        SampleEntity sampleEntity = new SampleEntity();
        sampleEntity.setId(documentId);
        sampleEntity.setType("test");
        sampleEntity.setRate(10);
        sampleEntity.setMessage("some message");
        repository.save(sampleEntity);

        String documentId2 = randomNumeric(5);
        SampleEntity sampleEntity2 = new SampleEntity();
        sampleEntity2.setId(documentId2);
        sampleEntity2.setType("test");
        sampleEntity2.setRate(20);
        sampleEntity2.setMessage("some message");
        repository.save(sampleEntity2);

        //when
        Page<SampleEntity> page = repository.findByRateLessThan(10, new PageRequest(1, 10));
        //then
        assertThat(page, is(notNullValue()));
        assertThat(page.getTotalElements(), is(equalTo(1L)));
    }

    @Test
    public void shouldExecuteCustomMethodWithBefore(){
        //given
        String documentId = randomNumeric(5);
        SampleEntity sampleEntity = new SampleEntity();
        sampleEntity.setId(documentId);
        sampleEntity.setType("test");
        sampleEntity.setRate(10);
        sampleEntity.setMessage("some message");
        repository.save(sampleEntity);

        //when
        Page<SampleEntity> page = repository.findByRateBefore(10, new PageRequest(1, 10));
        //then
        assertThat(page, is(notNullValue()));
        assertThat(page.getTotalElements(), is(equalTo(1L)));
    }

    @Test
    public void shouldExecuteCustomMethodWithAfter(){
        //given
        String documentId = randomNumeric(5);
        SampleEntity sampleEntity = new SampleEntity();
        sampleEntity.setId(documentId);
        sampleEntity.setType("test");
        sampleEntity.setRate(10);
        sampleEntity.setMessage("some message");
        repository.save(sampleEntity);

        //when
        Page<SampleEntity> page = repository.findByRateAfter(10, new PageRequest(1, 10));
        //then
        assertThat(page, is(notNullValue()));
        assertThat(page.getTotalElements(), is(equalTo(1L)));
    }

    @Test
    public void shouldExecuteCustomMethodWithLike(){
        //given
        String documentId = randomNumeric(5);
        SampleEntity sampleEntity = new SampleEntity();
        sampleEntity.setId(documentId);
        sampleEntity.setType("test");
        sampleEntity.setRate(10);
        sampleEntity.setMessage("foo");
        repository.save(sampleEntity);

        //when
        Page<SampleEntity> page = repository.findByMessageLike("fo", new PageRequest(1, 10));
        //then
        assertThat(page, is(notNullValue()));
        assertThat(page.getTotalElements(), is(equalTo(1L)));
    }

    @Test
    public void shouldExecuteCustomMethodForStartingWith(){
        //given
        String documentId = randomNumeric(5);
        SampleEntity sampleEntity = new SampleEntity();
        sampleEntity.setId(documentId);
        sampleEntity.setType("test");
        sampleEntity.setRate(10);
        sampleEntity.setMessage("foo");
        repository.save(sampleEntity);

        //when
        Page<SampleEntity> page = repository.findByMessageStartingWith("fo", new PageRequest(1, 10));
        //then
        assertThat(page, is(notNullValue()));
        assertThat(page.getTotalElements(), is(equalTo(1L)));
    }

    @Test
    public void shouldExecuteCustomMethodForEndingWith(){
        //given
        String documentId = randomNumeric(5);
        SampleEntity sampleEntity = new SampleEntity();
        sampleEntity.setId(documentId);
        sampleEntity.setType("test");
        sampleEntity.setRate(10);
        sampleEntity.setMessage("foo");
        repository.save(sampleEntity);

        //when
        Page<SampleEntity> page = repository.findByMessageEndingWith("o", new PageRequest(1, 10));
        //then
        assertThat(page, is(notNullValue()));
        assertThat(page.getTotalElements(), is(equalTo(1L)));
    }

    @Test
    public void shouldExecuteCustomMethodForContains(){
        //given
        String documentId = randomNumeric(5);
        SampleEntity sampleEntity = new SampleEntity();
        sampleEntity.setId(documentId);
        sampleEntity.setType("test");
        sampleEntity.setRate(10);
        sampleEntity.setMessage("foo");
        repository.save(sampleEntity);

        //when
        Page<SampleEntity> page = repository.findByMessageContaining("fo", new PageRequest(1, 10));
        //then
        assertThat(page, is(notNullValue()));
        assertThat(page.getTotalElements(), is(equalTo(1L)));
    }

//    @Test
//    @Ignore("Test failing due to java.lang.IllegalArgumentException: Invalid order syntax for part Message!")
//    public void shouldExecuteCustomMethodForIn(){
//        //given
//        String documentId = randomNumeric(5);
//        SampleEntity sampleEntity = new SampleEntity();
//        sampleEntity.setId(documentId);
//        sampleEntity.setType("test");
//        sampleEntity.setMessage("foo");
//        repository.save(sampleEntity);
//
//        //given
//        String documentId2 = randomNumeric(5);
//        SampleEntity sampleEntity2 = new SampleEntity();
//        sampleEntity2.setId(documentId2);
//        sampleEntity2.setType("test");
//        sampleEntity2.setMessage("bar");
//        repository.save(sampleEntity2);
//
//        List<String> ids = Arrays.asList(documentId,documentId2);
//
//
//        //when
//        Page<SampleEntity> page = repository.findByIdIn(ids, new PageRequest(1, 10));
//        //then
//        assertThat(page, is(notNullValue()));
//        assertThat(page.getTotalElements(), is(equalTo(2L)));
//    }
//
//    @Test
//    @Ignore("Test failing due to java.lang.IllegalArgumentException: Invalid order syntax for part Message!")
//    public void shouldExecuteCustomMethodForNotIn(){
//        //given
//        String documentId = randomNumeric(5);
//        SampleEntity sampleEntity = new SampleEntity();
//        sampleEntity.setId(documentId);
//        sampleEntity.setType("test");
//        sampleEntity.setMessage("foo");
//        repository.save(sampleEntity);
//
//        //given
//        String documentId2 = randomNumeric(5);
//        SampleEntity sampleEntity2 = new SampleEntity();
//        sampleEntity2.setId(documentId2);
//        sampleEntity2.setType("test");
//        sampleEntity2.setMessage("bar");
//        repository.save(sampleEntity2);
//
//        List<String> ids = Arrays.asList(documentId);
//
//
//        //when
//        Page<SampleEntity> page = repository.findByIdNotIn(ids, new PageRequest(1, 10));
//        //then
//        assertThat(page, is(notNullValue()));
//        assertThat(page.getTotalElements(), is(equalTo(1L)));
//        assertThat(page.getContent().get(0).getId(),is(documentId2));
//    }
//
//    @Test
//    @Ignore("Test failing due to java.lang.IllegalArgumentException: Invalid order syntax for part Message!")
//    public void shouldExecuteCustomMethodForTrue(){
//        //given
//        String documentId = randomNumeric(5);
//        SampleEntity sampleEntity = new SampleEntity();
//        sampleEntity.setId(documentId);
//        sampleEntity.setType("test");
//        sampleEntity.setMessage("foo");
//        sampleEntity.setAvailable(true);
//        repository.save(sampleEntity);
//
//        //given
//        String documentId2 = randomNumeric(5);
//        SampleEntity sampleEntity2 = new SampleEntity();
//        sampleEntity2.setId(documentId2);
//        sampleEntity2.setType("test");
//        sampleEntity2.setMessage("bar");
//        sampleEntity2.setAvailable(false);
//        repository.save(sampleEntity2);
//        //when
//        Page<SampleEntity> page = repository.findByAvailableTrue(new PageRequest(1, 10));
//        //then
//        assertThat(page, is(notNullValue()));
//        assertThat(page.getTotalElements(), is(equalTo(1L)));
//    }
//
//    @Test
//    @Ignore("Test failing due to java.lang.IllegalArgumentException: Invalid order syntax for part Message!")
//    public void shouldExecuteCustomMethodForFalse(){
//        //given
//        String documentId = randomNumeric(5);
//        SampleEntity sampleEntity = new SampleEntity();
//        sampleEntity.setId(documentId);
//        sampleEntity.setType("test");
//        sampleEntity.setMessage("foo");
//        sampleEntity.setAvailable(true);
//        repository.save(sampleEntity);
//
//        //given
//        String documentId2 = randomNumeric(5);
//        SampleEntity sampleEntity2 = new SampleEntity();
//        sampleEntity2.setId(documentId2);
//        sampleEntity2.setType("test");
//        sampleEntity2.setMessage("bar");
//        sampleEntity2.setAvailable(false);
//        repository.save(sampleEntity2);
//        //when
//        Page<SampleEntity> page = repository.findByAvailableFalse(new PageRequest(1, 10));
//        //then
//        assertThat(page, is(notNullValue()));
//        assertThat(page.getTotalElements(), is(equalTo(1L)));
//    }
//
//    @Test
//    @Ignore("Test failing due to java.lang.IllegalArgumentException: Invalid order syntax for part Message!")
//    public void shouldExecuteCustomMethodForOrderBy(){
//        //given
//        String documentId = randomNumeric(5);
//        SampleEntity sampleEntity = new SampleEntity();
//        sampleEntity.setId(documentId);
//        sampleEntity.setType("test");
//        sampleEntity.setMessage("foo");
//        sampleEntity.setAvailable(true);
//        repository.save(sampleEntity);
//
//        //given
//        String documentId2 = randomNumeric(5);
//        SampleEntity sampleEntity2 = new SampleEntity();
//        sampleEntity2.setId(documentId2);
//        sampleEntity2.setType("test");
//        sampleEntity2.setMessage("bar");
//        sampleEntity2.setAvailable(false);
//        repository.save(sampleEntity2);
//        //when
//        Page<SampleEntity> page = repository.findByMessageOrderByMessage("foo",new PageRequest(1, 10));
//        //then
//        assertThat(page, is(notNullValue()));
//        assertThat(page.getTotalElements(), is(equalTo(1L)));
//    }
//
//    @Test
//    public void testCustomMethodForBoolean(){
//        //given
//        String documentId = randomNumeric(5);
//        SampleEntity sampleEntity = new SampleEntity();
//        sampleEntity.setId(documentId);
//        sampleEntity.setType("test");
//        sampleEntity.setMessage("foo");
//        sampleEntity.setAvailable(true);
//        repository.save(sampleEntity);
//
//        //given
//        String documentId2 = randomNumeric(5);
//        SampleEntity sampleEntity2 = new SampleEntity();
//        sampleEntity2.setId(documentId2);
//        sampleEntity2.setType("test");
//        sampleEntity2.setMessage("bar");
//        sampleEntity2.setAvailable(false);
//        repository.save(sampleEntity2);
//        //when
//        Page<SampleEntity> page = repository.findByAvailable(false,new PageRequest(1, 10));
//        //then
//        assertThat(page, is(notNullValue()));
//        assertThat(page.getTotalElements(), is(equalTo(1L)));
//    }

}
