package org.example.person;

import org.junit.jupiter.api.BeforeEach;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.List;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FriendshipsMongoMockitoTest {
    @Mock
    private FriendsCollection friendsCollection;

    @InjectMocks
    private FriendshipsMongo friendshipsMongo = new FriendshipsMongo();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void alexDoesNotHaveFrieds() {
        when(friendsCollection.findByName("Alex")).thenReturn(null);
        assertThat(friendshipsMongo.getFriendsList("Alex")).isEmpty();
        verify(friendsCollection).findByName("Alex");
    }

    @Test
    public void mockWorksAsExpected() {
        Person joe = new Person("Joe");
        when(friendsCollection.findByName("Joe")).thenReturn(joe);
        assertThat(friendsCollection.findByName("Joe")).isEqualTo(joe);
        verify(friendsCollection).findByName("Joe");
    }

    @Test
    public void alexDoesNotHaveFriends() {
        Person alex = new Person("Alex");
        when(friendsCollection.findByName("Alex")).thenReturn(alex);
        assertThat(friendshipsMongo.getFriendsList("Alex")).isEmpty();
        verify(friendsCollection).findByName("Alex");
    }

    @Test
    public void joeHas5Friends(){
        List<String> expected = Arrays.asList(new String[]{"Karol","Dawid","Maciej","Tomek","Adam"});
        Person joe = new Person("Joe");
        when(friendsCollection.findByName("Joe")).thenReturn(joe);
        expected.forEach(name -> {
            friendshipsMongo.makeFriends("Joe", name);
        });
        assertThat(friendshipsMongo.getFriendsList("Joe")).hasSize(5).containsOnly("Karol","Dawid","Maciej","Tomek","Adam");
        verify(friendsCollection, atLeast(5)).findByName("Joe");
    }

    @Test
    public void BartHasNoFriends(){
        List<String> expected = Arrays.asList(new String[]{});
        Person bart = new Person("Bart");
        when(friendsCollection.findByName("Bart")).thenReturn(bart);
        expected.forEach(name -> {
            friendshipsMongo.makeFriends("Bart", name);
        });
        assertThat(friendshipsMongo.getFriendsList("Bart")).isEmpty();
        verify(friendsCollection).findByName("Bart");
    }
    @Test
    public void areFriends() {
        Person joe = new Person("Joe");
        when(friendsCollection.findByName("Joe")).thenReturn(joe);
        joe.addFriend("Bart");
        assertThat(friendshipsMongo.areFriends("Joe", "Bart")).isTrue();
        verify(friendsCollection).findByName("Joe");
    }

    @Test
    public void areNoFriends() {
        Person joe = new Person("Joe");
        when(friendsCollection.findByName("Joe")).thenReturn(joe);
        assertThat(friendshipsMongo.areFriends("Joe", "Bart")).isFalse();
        verify(friendsCollection).findByName("Joe");
    }

    @Test
    public void notOnAList() {
        Person mark = new Person();
        when(friendsCollection.findByName("Mark")).thenReturn(mark);
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> friendshipsMongo.areFriends("Mark", "John"));
        verify(friendsCollection).findByName("Mark");
    }

    @Test
    public void isNotEmpty() {
        List<String> joesFriends = Arrays.asList(new String[]{"Bart"});
        Person joe = new Person("Joe");
        when(friendsCollection.findByName("Joe")).thenReturn(joe);
        joesFriends.forEach(name -> {
            friendshipsMongo.makeFriends("Joe", name);
        });
        assertThat(friendshipsMongo.getFriendsList("Joe")).isNotEmpty();
        verify(friendsCollection, times(2)).findByName("Joe");
    }
}