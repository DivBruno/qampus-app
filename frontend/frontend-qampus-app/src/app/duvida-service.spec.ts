import { TestBed } from '@angular/core/testing';

import { DuvidaService } from './duvida-service';
import { Duvida } from './duvida';

describe('DuvidaService', () => {
  let service: DuvidaService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DuvidaService);
  });

  it('should create new post succesfully', async() => {
    const duvida: Duvida = {
      title: 'TESTE',
      content: 'TESTES',
      tags: []
    }
    const token = 'token-teste';
    vi.spyOn(globalThis, 'fetch').mockResolvedValue(
      new Response(
        JSON.stringify(duvida),
        {
          status: 200,
          headers: {'Content-Type': 'application/json',
            'Authorization': 'Bearer '+token
          }
        }
      )
    )
    const result = await service.createDuvida(duvida);
    expect(result).toBe(true);
  });

  it('should return false when creating a new post fails', async()=>{
    vi.spyOn(globalThis, 'fetch').mockResolvedValue(
      new Response(null, {status: 401})
    );
    const duvida: Duvida = {
      title: 'TESTE',
      content: 'TESTES',
      tags: []
    }

    const result = await service.createDuvida(duvida);
    expect(result).toBe(false);
  })

  it('should return false when creating a new post throws an error', async()=>{
    vi.spyOn(globalThis, 'fetch').mockRejectedValue(
      new Error('Erro de conexão')
    );
    const duvida: Duvida = {
      title: 'TESTE',
      content: 'TESTES',
      tags: []
    }
    const result = await service.createDuvida(duvida);
    expect(result).toBe(false);
  })
});
