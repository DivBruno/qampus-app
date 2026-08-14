import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormControl, FormGroup, FormsModule, Validators, ReactiveFormsModule } from '@angular/forms';
import { Navbar } from "../../navbar/navbar";
import { PostService, NewPost, Post, EditPost} from '../post-service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-create-post',
  imports: [FormsModule, ReactiveFormsModule, Navbar],
  templateUrl: './create-post.html',
  styleUrl: './create-post.css',
})

export class CreatePost implements OnInit{
  constructor(
    private route: ActivatedRoute,
    private postService: PostService,
    private router: Router
  ){}
  
  post: Post = {
    id: '',
    title: '',
    content: '',
    upVotes: 0,
    downVotes: 0,
    tags: [],
    createdAt: ''
  }

  novaTag = false;
  nomeTag = '';
  quantidadeTags = 0;
  tagsCriadas: string[] = [];
  text= '';
  editar = false;

  postForm = new FormGroup({
    title: new FormControl('', Validators.required),
    content: new FormControl('', Validators.required)
  })

  async ngOnInit(): Promise<void> {
    const id = this.route.snapshot.paramMap.get('id');

    if(id){
      const postEncontrado = await this.postService.findById(id);
      if(postEncontrado){
        this.post = postEncontrado
        this.postForm.setValue({
          title: this.post.title,
          content: this.post.content
        })
        for (let index = 0; index < this.post.tags.length; index++) {
          this.nomeTag = this.post.tags[index].name;
          this.adicionarTag();
        }
        this.text = 'Editar'
        this.editar = true
      }
    }else{
      this.text = 'Publicar';
    }
  }

  abrirFecharTag() {
    this.novaTag = !this.novaTag;
  }

  adicionarTag() {
      if (this.nomeTag.trim() !== '') {
          this.tagsCriadas.push(this.nomeTag.trim());
          this.nomeTag = '';
          this.novaTag = false;
      }
  }

  goTo(rota: string){
    this.router.navigate([rota]);
  }

  removeTag(tag: string){
    const tagRemovida = this.tagsCriadas.indexOf(tag);
    this.tagsCriadas.splice(tagRemovida, 1);
  }

  async submit(){
    if(this.postForm.valid){
      if(this.editar){
        this.editarPost();
      }else{
        this.criarPost();
      }
    }else{
      alert("Todos os dados devem estar preenchidos");
    }
  }

  async editarPost(){
    const editPost:EditPost = {
      id: this.post.id,
      title: this.postForm.value.title!,
      content: this.postForm.value.content!,
      tags: this.tagsCriadas
    }
    const response = await this.postService.editPost(editPost);
    if(response){
      this.router.navigate(['home']);
    }else{
      alert("Erro ao editar a dúvida")
    }
  }

  async criarPost(){
    const post: NewPost = {
      title: this.postForm.value.title!,
      content: this.postForm.value.content!,
      tags: this.tagsCriadas
    }
    const response = await this.postService.createPost(post);
    if(response){
      this.router.navigate(['home']);
    }else{
      alert("Erro ao publicar uma nova dúvida");
    }
  }
}
