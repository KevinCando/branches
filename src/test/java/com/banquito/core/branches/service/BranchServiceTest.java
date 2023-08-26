package com.banquito.core.branches.service;

import com.banquito.core.branches.exception.CRUDException;
import com.banquito.core.branches.model.Branch;
import com.banquito.core.branches.repository.BranchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BranchServiceTest {

    @Mock
    private BranchRepository branchRepository;

    @InjectMocks
    private BranchService branchService;

    private String id;
    private Branch branch;
    private Branch branch1;
    private List<Branch> branches;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        id = "112";
        branch = new Branch();
        branch.setId(id);
        branch.setCode("123411");
        branch.setName("Sucursal 1");
        branch1 = new Branch();
        branch1.setId("1234");
        branch1.setCode("223411");
        branch1.setName("Sucursal 2");
        branches = new ArrayList<>();
        branches.add(branch);
        branches.add(branch1);

    }


    @Test
    void lookByIdCRUDException() {
        when(branchRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(CRUDException.class, () -> {
           branchService.lookById(id);
        });
        verify(branchRepository,never()).save(any(Branch.class));
        verify(branchRepository, times(1)).findById(id);
    }

    @Test
    void lookByCode() {
        when(branchRepository.findByCode(branch.getCode())).thenReturn(branch);

        Branch result = branchService.lookByCode(branch.getCode());

        assertEquals(branch, result);
        verify(branchRepository, times(1)).findByCode(branch.getCode());
    }

    @Test
    void getAll() {
        when(branchRepository.findAll()).thenReturn(branches);
        List<Branch> branchesRS = branchService.getAll();
        assertEquals(branches, branchesRS);
    }

    @Test
    void create() {
        BranchRepository customMock = mock(BranchRepository.class);
        class WrappedException extends RuntimeException {
            WrappedException(CRUDException e) {
                super(e);
            }
        }

        when(customMock.save(branch)).thenThrow(new WrappedException(new CRUDException(510, "Exception")));

        BranchService branchServiceWithCustomMock = new BranchService(customMock);
        assertThrows(CRUDException.class, () -> branchServiceWithCustomMock.create(branch));

        verify(customMock, times(1)).save(branch);
    }

    @Test
    void update() {
        when(branchRepository.findByCode(branch.getCode())).thenReturn(branch);
        class WrappedException extends RuntimeException {
            WrappedException(CRUDException e) {
                super(e);
            }
        }

        when(branchRepository.save(branch)).thenThrow(new WrappedException(new CRUDException(520, "Exception")));

        assertThrows(CRUDException.class, () -> branchService.update(branch.getCode(), branch1));

        verify(branchRepository, times(1)).findByCode(branch.getCode());

        verify(branchRepository, times(1)).save(branch);
    }

}